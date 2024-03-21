package clientTests;

import exception.ResponseException;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {
    private static Server server;
    static ServerFacade facade;

    private String authToken;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+ port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() throws ResponseException {
        facade.clear();
    }

    @Test
    public void clearTest() throws ResponseException {
        var userData = new UserData("player2", "password2", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var userData2 = new UserData("player2", "password2", null);
        facade.clear();
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login(userData);});
    }

    @Test
    void registerPositive() throws ResponseException {
        var userData = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(userData);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerNegative() throws ResponseException {
        var userData = new UserData("player1", "password", null);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.register(userData);});
    }
    @Test
    void loginPositive() throws ResponseException {
        var userData = new UserData("player2", "password2", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var userData2 = new UserData("player2", "password2", null);
        var authData2 = facade.login(userData2);
        assertTrue(authData2.authToken().length() > 10);
    }

    @Test
    void loginNegative() throws ResponseException {
        var userData = new UserData("player1", null, null);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login(userData);});
    }
    @Test
    void createGamesPositive() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var userData2 = new UserData("player2", "123", null);
        var authData2 = facade.login(userData);
        var gameData = new GameData(0, null, null, "MySuperCoolGame", null);
        var game = facade.createGame(authData2.authToken(), gameData);
        assertTrue(game.gameID() >= 1);
    }

    @Test
    void createGamesNegative() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var userData2 = new UserData("player2", "123", null);
        var authData2 = facade.login(userData);
        var gameData = new GameData(0, null, null, null, null);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.createGame(authData2.authToken(), gameData);});
    }
    @Test
    void listGamesPositive() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var userData2 = new UserData("player2", "123", null);
        var authData2 = facade.login(userData);
        var gameData = new GameData(0, null, null, "MySuperCoolGame", null);
        var game = facade.createGame(authData2.authToken(), gameData);
        var listOfGames = facade.listGames(authData2.authToken());
        assertFalse(listOfGames.games().isEmpty());
    }

    @Test
    void listGamesNegative() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var userData2 = new UserData("player2", "123", null);
        var authData2 = facade.login(userData);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.listGames(authData.authToken());});
    }

    @Test
    void joinGamesPositive() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var userData2 = new UserData("player2", "123", null);
        var authData2 = facade.login(userData);
        var gameData = new GameData(0, null, null, "MySuperCoolGame", null);
        var game = facade.createGame(authData2.authToken(), gameData);
        JoinGameData joinGameData = new JoinGameData("WHITE", game.gameID());
        Assertions.assertDoesNotThrow(() -> {
            facade.joinGame(authData2.authToken(), joinGameData);});
    }
    @Test
    void joinGamesNegative() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var authData2 = facade.login(userData);
        JoinGameData joinGameData = new JoinGameData("WHITE", 2);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.joinGame(authData2.authToken(), joinGameData);});
    }

    @Test
    void observeGamePositive() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var userData2 = new UserData("player2", "123", null);
        var authData2 = facade.login(userData);
        var gameData = new GameData(0, null, null, "MySuperCoolGame", null);
        var game = facade.createGame(authData2.authToken(), gameData);
        JoinGameData joinGameData = new JoinGameData(null, game.gameID());
        Assertions.assertDoesNotThrow(() -> {
            facade.joinGame(authData2.authToken(), joinGameData);});
    }
    @Test
    void observeGameNegative() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var authData2 = facade.login(userData);
        JoinGameData joinGameData = new JoinGameData(null, 2);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.joinGame(authData2.authToken(), joinGameData);});
    }

    @Test
    void logoutPositive() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        var authData = facade.register(userData);
        facade.logout(authData.authToken());
        var authData2 = facade.login(userData);
        var gameData = new GameData(0, null, null, "MySuperCoolGame", null);
        var game = facade.createGame(authData2.authToken(), gameData);
        JoinGameData joinGameData = new JoinGameData("WHITE", game.gameID());
        facade.logout(authData2.authToken());
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.joinGame(authData2.authToken(), joinGameData);});
    }
    @Test
    void logoutNegative() throws ResponseException {
        var userData = new UserData("player2", "123", "p1@email.com2");
        facade.register(userData);
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.logout("hehe Im not an authToken");});
    }
}
