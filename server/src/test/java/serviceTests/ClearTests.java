package serviceTests;

import dataAccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.SessionService;
import service.UserService;
import serviceHandler.SessionResource;

import java.util.ArrayList;


public class ClearTests {
    @Test
    public void positiveClear() throws DataAccessException{
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
        var username = authDAO.getUsername(authData.authToken());
        Assertions.assertEquals(1, games.games().size());
        Assertions.assertEquals(authData.username(), username);
        gameService.clear();
        games = gameDAO.listGames();
        Assertions.assertEquals(0, games.games().size());
        Assertions.assertThrows(DataAccessException.class, ()->{authDAO.getUsername(authData.authToken());});
        Assertions.assertThrows(DataAccessException.class, ()->{userDAO.getUser(username);});
    }
}
