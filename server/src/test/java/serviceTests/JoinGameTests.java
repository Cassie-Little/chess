package serviceTests;

import dataAccess.*;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

public class JoinGameTests {
    @Test
    public void positiveJoinGame() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameData gameData = new GameData(1, null, "a", "name", null);
        UserData userData = new UserData("a", "a", "a");
        GameService gameService = new GameService(gameDAO, userDAO, authDAO);
        UserService userService = new UserService(userDAO, authDAO);
        var authData = userService.register(userData);
        var gameID = gameService.createGame(authData.authToken(), gameData);
        JoinGameData joinGameData = new JoinGameData("WHITE", gameID);
        gameService.joinGame(authData.authToken(), joinGameData);
        GameData updatedGameData = gameDAO.getGame(gameID);
        Assertions.assertNotNull(updatedGameData);
        Assertions.assertEquals("a", updatedGameData.whiteUsername());
    }

    @Test
    public void negativeJoinGame() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameData gameData = new GameData(1, null, "a", "name", null);
        UserData userData = new UserData("a", "a", "a");
        GameService gameService = new GameService(gameDAO, userDAO, authDAO);
        UserService userService = new UserService(userDAO, authDAO);
        var authData = userService.register(userData);
        var gameID = gameService.createGame(authData.authToken(), gameData);
        JoinGameData joinGameData = new JoinGameData("pookie", gameID);
        String fakeAuth = "gotcha!";
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(authData.authToken(), joinGameData);
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(fakeAuth, joinGameData);
        });
    }
}
