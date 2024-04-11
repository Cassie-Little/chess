package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import webSocketMessages.serverMessages.GameError;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class Client implements NotificationHandler {
    private String player = null;
    private ServerFacade server;
    private WebSocketFacade webSocket;
    private State state = State.LOGGEDOUT;
    private AuthData authData;
    private final PrintStream out;
    private final String serverURL;


    private final Scanner scanner;
    private int gameID;
    private ChessGame.TeamColor teamColor =  ChessGame.TeamColor.WHITE;



    public Client(String serverURL) throws ResponseException {
        this.serverURL =serverURL;
        this.scanner = new Scanner(System.in);
        this.webSocket = new WebSocketFacade(serverURL, this);
        server = new ServerFacade(serverURL);
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "list" -> listGames();
                case "logout" -> logout();
                case "join" -> joinGame(params);
                case "observe" -> joinAsObserver(params);
                case "create" -> createGames(params);
                case "clear" -> clear();
                case "quit" -> "quit";
                case "help" -> help();
                case "highlight" -> highlight(params);
                case "redraw" -> redrawBoard(params);
                case "resign" -> resign(params);
                case "leave" -> leave(params);
                case "move" -> makeMove(params);
                default -> help();
            };
        } catch (exception.ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String clear() throws ResponseException {
        assertLoggedIn();
        server.clear();
        return "deleted all games and users";
    }

    public String highlight(String... params) throws ResponseException {
        assertObserveGame();
        if (params.length == 1) {
            var chessPosition = parsePosition(params[0]);
            var gameData = server.getGame(authData.authToken(), gameID);
            var teamColor = gameData.blackUsername().equals(authData.username()) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
            ChessBoardUI.displayLegalMoves(out, gameData.game().getBoard(), teamColor, chessPosition);
        }
        throw new exception.ResponseException(400, "Expected: <position>");
    }

    public String redrawBoard(String... params) throws ResponseException {
        assertObserveGame();
        if (params.length == 1) {
            String teamColorString = params[0].toUpperCase();
            var teamColor = Enum.valueOf(ChessGame.TeamColor.class, teamColorString);
            var gameData = server.getGame(authData.authToken(), gameID);
            ChessBoardUI.displayBoard(out,gameData.game().getBoard(), teamColor);
            return "here's your board";
        }
        throw new exception.ResponseException(400, "Expected: <position>");
    }

    public String leave(String... params) throws ResponseException {
        assertObserveGame();
        if (params.length != 0){
            throw new ResponseException(400, "Expected: nothing");
        }
        webSocket.leave(new Leave(authData.authToken(), gameID));
        return help();
    }

    public String resign(String... params) throws ResponseException {
        assertPlayGame();
        if (params.length == 0) {
            System.out.print("Are you sure you want to resign?");
            var input = scanner.nextLine().toLowerCase();
            if (input.equals("yes")) {
                webSocket.resign(new Resign(authData.authToken(), gameID));
            }
            return "resigned";
        }
        throw new ResponseException(400, "Expected: nothing");
    }

    public String makeMove(String... params) throws ResponseException {
        assertPlayGame();
        if (params.length != 2 && params.length != 3) {
            throw new ResponseException(400, "Expected  <start position> <end position>");
        }
        var startPosition = parsePosition(params[0]);
        var endPosition = parsePosition(params[1]);
        ChessPiece.PieceType pieceType = null;
        if (params.length == 3) {
            var pieceString  = params[2].toUpperCase();
            pieceType = Enum.valueOf(ChessPiece.PieceType.class, pieceString);
        }
        ChessMove chessMove = new ChessMove(startPosition, endPosition, pieceType);
        webSocket.makeMove(new MakeMove(authData.authToken(), gameID, chessMove));
        return "move sent";
    }

    private static ChessPosition parsePosition(String positionStr) throws ResponseException {
        positionStr=  positionStr.toLowerCase();
        if (positionStr.length() != 2){
            throw new ResponseException(400, "Expected: valid position");
        }
        var col = positionStr.charAt(0) - 96;
        var row = positionStr.charAt(1) - 48;
        return new ChessPosition(row, col);
    }

    public String login(String... params) throws exception.ResponseException {
        assertLoggedOut();
        if (params.length == 2) {
            var userData = new UserData(params[0], params[1], null);
            authData = server.login(userData);
            state = State.LOGGEDIN;
            webSocket = new WebSocketFacade(serverURL, this);
            return String.format("You signed in as %s. Press enter for more options \uD83D\uDC97", params[0]);
        }
        throw new exception.ResponseException(400, "Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        assertLoggedOut();
        if (params.length == 3) {
            var userData = new UserData(params[0], params[1], params[2]);
            authData = server.register(userData);
            state = State.LOGGEDIN;
            return String.format("You registered and logged in as %s. Press enter for more options \uD83D\uDC97", params[0]);
        } else {
            throw new ResponseException(400, "Expected: <username> <password> <email>");
        }
    }

    public String logout() throws ResponseException {
        assertLoggedIn();
        state = State.LOGGEDOUT;
        server.logout(authData.authToken());
        webSocket = null;
        return String.format("You have logged out", authData.username());

    }

    public String listGames() throws ResponseException {
        assertLoggedIn();
        var gameList = server.listGames(authData.authToken());
        if (gameList.toString().isEmpty()) {
            return "Please create a game";
        } else {
            String listedGames = formatNumberedList(gameList);
            return listedGames;
        }
    }

    private String formatNumberedList(model.GameListData gameList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < gameList.games().size(); i++) {
            stringBuilder.append((i + 1) + ". " + gameList.games().get(i) + "\n");
        }
        return stringBuilder.toString();
    }

    public String createGames(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            var gameData = new GameData(0, null, null, params[0], null);
            var gameID = server.createGame(authData.authToken(), gameData).gameID();
            return String.format("you game ID is: %s", gameID);
        }
        throw new ResponseException(400, "Expected: <game_name>");
    }


    public String joinGame(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            return joinAsObserver(params);
        } else if (params.length == 2) {
            gameID = Integer.parseInt(params[0]);
            teamColor = Enum.valueOf(ChessGame.TeamColor.class, params[1].toUpperCase());
            var joinGameData = new JoinGameData(params[1].toUpperCase(), gameID);
            server.joinGame(authData.authToken(), joinGameData);
            var gameData = server.getGame(authData.authToken(), gameID);
            webSocket.joinPlayer(new JoinPlayer(authData.authToken(), gameID, teamColor));
            state = State.PLAYGAME;
            return String.format("You're playing game: %s \n %s", gameData.gameName() , help());
        }
        throw new ResponseException(400, "Expected: <gameID> <player_color_(white/black/empty)>");
    }

    public String joinAsObserver(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            gameID = Integer.parseInt(params[0]);
            var joinGameData = new JoinGameData(null, gameID);
            server.joinGame(authData.authToken(), joinGameData);
            var gameData = server.getGame(authData.authToken(), gameID);
            webSocket.joinObserver(new JoinObserver(authData.authToken(), gameID ));
            ChessBoardUI.displayBoard(out, gameData.game().getBoard(), ChessGame.TeamColor.WHITE);
            ChessBoardUI.displayBoard(out, gameData.game().getBoard(), ChessGame.TeamColor.BLACK);
            state = State.OBSERVEGAME;
            return String.format("You're observing game: %s \n %s", gameData.gameName(), help());
        }
        throw new ResponseException(400, "Expected: <gameID>");
    }


    public String help() {
        return switch (state) {
            case LOGGEDOUT -> """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    - help
                    """;
            case LOGGEDIN -> """
                    - list
                    - create <enter a name>
                    - join <gameID> <enter black or white>
                    - observe <gameID>
                    - logout
                    - quit
                    - clear
                    - help
                    """;
            case PLAYGAME -> """
                    - redraw  <your color>
                    - highlight  <position (ex. a2)>
                    - move  <start position> <end position> [<promotion piece>]
                    - leave
                    - resign
                    - logout
                    - quit
                    - clear
                    - help
                    """;
            case OBSERVEGAME ->  """
                    - redraw  <your color>
                    - highlight  <position (ex. a2)>
                    - leave
                    - logout
                    - quit
                    - clear
                    - help
                    """;
        };
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "Please login or register :)");
        }
    }
    private void assertObserveGame() throws ResponseException {
        assertLoggedIn();
        if (state != State.OBSERVEGAME && state != State.PLAYGAME) {
            throw new ResponseException(400, "please join or observe a game");
        }
    }
    private void assertPlayGame() throws ResponseException {
        assertObserveGame();
        if (state != State.PLAYGAME) {
            throw new ResponseException(400, "please join or observe a game");
        }
    }
    private void assertLoggedOut() throws ResponseException {
        if (state != State.LOGGEDOUT) {
            throw new ResponseException(400, "You are logged in");
        }
    }


    @Override
    public void notify(Notification notification) {
        System.out.println(notification.getMessage());

    }

    @Override
    public void loadGame(LoadGame loadGame) {
        try {
            ChessBoardUI.displayBoard(out, loadGame.game().getBoard(), teamColor);
            System.out.println(String.format("Your game: %s", loadGame.getGameName()));
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void error(GameError gameError) {
        out.println("Error: " + gameError.getErrorMessage());
    }
}
