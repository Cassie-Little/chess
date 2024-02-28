package serviceTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.SessionService;
import service.UserService;

public class LoginTests {
    @Test
    public void positiveLoginTests() throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserData userData = new UserData("a", "a", "a");
        UserService userService = new UserService(userDAO, authDAO);
        SessionService sessionService = new SessionService(userDAO, authDAO);
        var authData = userService.register(userData);
        var loginData = sessionService.login(userData);
        Assertions.assertEquals(authData.username(), loginData.username());
    }
    @Test
    public void negativeLoginTests() throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserData userData = new UserData("a", "a", "a");
        UserService userService = new UserService(userDAO, authDAO);
        SessionService sessionService = new SessionService(userDAO, authDAO);
        userService.register(userData);
        userData = new UserData("haha", "hehe", "hehe");
        UserData finalUserData = userData;
        Assertions.assertThrows(DataAccessException.class, ()->{sessionService.login(finalUserData);});
    }

}
