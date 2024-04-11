package ui;

import chess.ChessGame;
import chess.ChessPosition;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import webSocketMessages.serverMessages.GameError;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.Resign;
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
                case "list_games" -> listGames();
                case "logout" -> logout();
                case "join_game" -> joinGame(params);
                case "observe_game" -> joinAsObserver(params);
                case "create_game" -> createGames(params);
                case "clear" -> clear();
                case "quit" -> "quit";
                case "help" -> help();
                case "highlight" -> highlight(params);
                case "redraw_board" -> redrawBoard(params);
                case "resign" -> resign(params);
                case "leave" -> leave(params);
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
        if (params.length == 2) {
            var gameID = Integer.parseInt(params[0]);
            var chessPosition = parsePosition(params);
            var gameData = server.getGame(authData.authToken(), gameID);
            var teamColor = gameData.blackUsername().equals(authData.username()) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
            ChessBoardUI.displayLegalMoves(out, gameData.game().getBoard(), teamColor, chessPosition);
        }
        throw new exception.ResponseException(400, "Expected: <position>");
    }

    public String redrawBoard(String... params) throws ResponseException {
        if (params.length == 2) {
            String teamColorString = params[1].toUpperCase();
            var teamColor = Enum.valueOf(ChessGame.TeamColor.class, teamColorString);
            var gameID = Integer.parseInt(params[0]);
            var gameData = server.getGame(authData.authToken(), gameID);
            ChessBoardUI.displayBoard(out,gameData.game().getBoard(), teamColor);
            return "here's your board";
        }
        throw new exception.ResponseException(400, "Expected: <gameID> <position>");
    }

    public String leave(String... params) throws ResponseException {
        if (params.length != 1){
            throw new ResponseException(400, "Expected: gameID");
        }
        var gameID = Integer.parseInt(params[0]);
        webSocket.leave(new Leave(authData.authToken(), gameID));
        return help();
    }

    public String resign(String... params) throws ResponseException {
        if (params.length == 1) {
            var gameID = Integer.parseInt(params[0]);
            System.out.print("Are you sure you want to resign?");
            var input = scanner.nextLine().toLowerCase();
            if (input.equals("yes")) {
                webSocket.resign(new Resign(authData.authToken(), gameID));
            }
            return "";
        }
        throw new ResponseException(400, "Expected: gameID");
    }


    private static ChessPosition parsePosition(String[] params) throws ResponseException {
        var positionStr = params[1].toLowerCase();
        if (positionStr.length() != 2){
            throw new ResponseException(400, "Expected: valid position");
        }
        var col = positionStr.charAt(0) - 96;
        var row = positionStr.charAt(1) - 48;
        return new ChessPosition(row, col);
    }

    public String login(String... params) throws exception.ResponseException {
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
        state = State.LOGGEDOUT;
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
        if (params.length >= 1) {
            var gameID = Integer.parseInt(params[0]);
            String teamColorString = params.length == 2 ? params[1].toUpperCase() : null;
            var joinGameData = new JoinGameData(teamColorString, gameID);
            server.joinGame(authData.authToken(), joinGameData);
            if (teamColorString != null) {
                var teamColor = Enum.valueOf(ChessGame.TeamColor.class, teamColorString);
                webSocket.joinPlayer(new JoinPlayer(authData.authToken(), gameID, teamColor ));
            }
            return "";
        }
        throw new ResponseException(400, "Expected: <gameID> <player_color_(white/black/empty)>");
    }

    public String joinAsObserver(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            var gameID = Integer.parseInt(params[0]);
            var joinGameData = new JoinGameData(null, gameID);
            server.joinGame(authData.authToken(), joinGameData);
            var gameData = server.getGame(authData.authToken(), gameID);
            webSocket.joinObserver(new JoinObserver(authData.authToken(), gameID ));
            ChessBoardUI.displayBoard(out, gameData.game().getBoard(), ChessGame.TeamColor.WHITE);
            ChessBoardUI.displayBoard(out, gameData.game().getBoard(), ChessGame.TeamColor.BLACK);
            return String.format("Your game: %s", gameData.gameName());
        }
        throw new ResponseException(400, "Expected: <gameID>");
    }


    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    - help
                    """;
        }
        return """
                - list_games
                - create_game <enter a name>
                - join_game <gameID> <enter black or white>
                - observe_game <gameID>
                - redraw_board <gameID> <your color>
                - highlight <gameID> <position (ex. a2)>
                - logout
                - quit
                - clear
                -help
                """;
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "Please login or register :)");
        }
    }


    @Override
    public void notify(Notification notification) {
        System.out.println(notification.getMessage());

    }

    @Override
    public void loadGame(LoadGame loadGame) {
        try {
            var blackUser = loadGame.getBlackPlayer();
            var teamColor = blackUser != null && blackUser.equals(player) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
            ChessBoardUI.displayBoard(out, loadGame.game().getBoard(), teamColor);
            System.out.println(String.format("Your game: %s", loadGame.getGameName()));
            System.out.flush();
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
