package serviceTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

public class ListGamesTests {
    @Test
    public void positiveListGames() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameData gameData = new GameData(1, "a", "a", "name", null);
        UserData userData = new UserData("a", "a", "a");
        GameService gameService = new GameService(gameDAO, userDAO, authDAO);
        UserService userService = new UserService(userDAO, authDAO);
        var authData = userService.register(userData);
        gameService.createGame(authData.authToken(), gameData);
        var games = gameService.listGames(authData.authToken());
        Assertions.assertEquals(1, games.games().size());
    }

    @Test
    public void negativeListGames() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameData gameData = new GameData(1, "a", "a", "name", null);
        UserData userData = new UserData("a", "a", "a");
        GameService gameService = new GameService(gameDAO, userDAO, authDAO);
        UserService userService = new UserService(userDAO, authDAO);
        var authData = userService.register(userData);
        var authToken = authData.authToken();
        gameService.createGame(authToken, gameData);
        authToken = "urMom";
        String finalAuthToken = authToken;
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames(finalAuthToken);
        });

    }

}
