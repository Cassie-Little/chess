package ui;

import exception.ResponseException;
import java.util.Arrays;

public class Client {
    private String player = null;
    private final String serverURL;
    private ServerFacade serverFacade;
    private State state = State.LOGGEDOUT;

    public Client(String serverURL) {
        this.serverURL = serverURL;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                //case "register" -> rescuePet(params);
                //case "listgames" -> listPets();
                //case "logout" -> signOut();
                //case "joingame" -> adoptPet(params);
                //case "creategame" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (exception.ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws exception.ResponseException {
        if (params.length >= 1) {
            state = State.LOGGEDIN;
            player = String.join("-", params);
            serverFacade = new ServerFacade(serverURL);
            //serverFacade.enterPetShop(player);
            return String.format("You signed in as %s.", player);
        }
        throw new exception.ResponseException(400, "Expected: <yourname>");
    }

    public  String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - login <yourname> <password>
                    - quit
                    """;
        }
        return """
                - register
                - login
                - list_games
                - create_games
                - join_game
                - logout
                - quit
                """;
    }
    private void assertLoggedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "Please sign in :)");
        }
    }

}
