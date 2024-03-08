package serviceTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

public class CreateGameTests {
    @Test
    public void positiveCreateGames() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameData gameData = new GameData(1, "a", "a", "name", null);
        UserData userData = new UserData("a", "a", "a");
        GameService gameService = new GameService(gameDAO, userDAO, authDAO);
        UserService userService = new UserService(userDAO, authDAO);
        var authData = userService.register(userData);
        var game = gameService.createGame(authData.authToken(), gameData);
        Assertions.assertEquals(gameDAO.getGame(game).gameName(), gameData.gameName());
    }

    @Test
    public void negativeCreateGames() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameData gameData = new GameData(0, null, null, null, null);
        UserData userData = new UserData("a", "a", "a");
        GameService gameService = new GameService(gameDAO, userDAO, authDAO);
        UserService userService = new UserService(userDAO, authDAO);
        var authData = userService.register(userData);
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(authData.authToken(), gameData);
        });
    }
}
