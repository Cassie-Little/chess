package ui;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Arrays;

public class Client {
    private String player = null;
    private ServerFacade server;
    private State state = State.LOGGEDOUT;
    private AuthData authData;

    public Client(String serverURL) {
        server = new ServerFacade(serverURL);
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
                case "create_game" -> createGames(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (exception.ResponseException ex) {
            return ex.getMessage();
        }
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
        if (state == State.LOGGEDIN) {
            state = State.LOGGEDOUT;
            server.logout();
            return String.format("You have logged out", authData.username());
        } else {
            throw new ResponseException(400, "You are not logged in");
        }
    }

    public String listGames() throws ResponseException {
        var gameList = server.listGames();
        if (gameList.toString().isEmpty()) {
            return "Please create a game";
        } else {
            return gameList.toString();
        }
    }

    public String createGames(String... params) throws ResponseException {
        if (params.length == 1) {
            var gameData = new GameData(0, null, null, params[0], null);
            int gameID = server.createGame(gameData).gameID();
            return String.format("you game ID is: ", gameID);
        }
        throw new ResponseException(400, "Expected: <game_name>");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            var gameBoard = server.joinGame();
            return String.format("Your game" + gameBoard);
        }
        throw new ResponseException(400, "Expected: <gameID> <player_color_(white/black/observer)>");
    }


    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    """;
        }
        return """
                - list_games
                - create_game <enter a name>
                - join_game <gameID> <enter black or white or observe>
                - logout
                - quit
                """;
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "Please login or register :)");
        }
    }

}
