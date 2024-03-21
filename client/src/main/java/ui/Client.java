package ui;

import chess.ChessBoard;
import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Client {
    private String player = null;
    private ServerFacade server;
    private State state = State.LOGGEDOUT;
    private AuthData authData;
    private PrintStream out;


    public Client(String serverURL) {
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

    public String login(String... params) throws exception.ResponseException {
        if (params.length == 2) {
            var userData = new UserData(params[0], params[1], null);
            authData = server.login(userData);
            state = State.LOGGEDIN;
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
            var game = new ChessGame();
            var board = new ChessBoard();
            board.resetBoard();
            game.setBoard(board);
            var gameData = new GameData(0, null, null, params[0], game);
            int gameID = server.createGame(authData.authToken(), gameData).gameID();
            return String.format("you game ID is: %s", gameID);
        }
        throw new ResponseException(400, "Expected: <game_name>");
    }


    public String joinGame(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length >= 1) {
            int gameID = Integer.parseInt(params[0]);
            String teamColorString = params.length == 2 ? params[1].toUpperCase() : null;
            var joinGameData = new JoinGameData(teamColorString, gameID);
            server.joinGame(authData.authToken(), joinGameData);
            var gameData = server.getGame(authData.authToken(), gameID);
            ChessBoardUI.displayBoard(out, gameData.game().getBoard(), ChessGame.TeamColor.WHITE);
            ChessBoardUI.displayBoard(out, gameData.game().getBoard(), ChessGame.TeamColor.BLACK);
            return String.format("Your game: %s", gameData.gameName());
        }
        throw new ResponseException(400, "Expected: <gameID> <player_color_(white/black/empty)>");
    }

    public String joinAsObserver(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            int gameID = Integer.parseInt(params[0]);
            var joinGameData = new JoinGameData(null, gameID);
            server.joinGame(authData.authToken(), joinGameData);
            var gameData = server.getGame(authData.authToken(), gameID);
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

}
