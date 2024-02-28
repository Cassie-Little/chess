package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

public class RegisterTests {
    @Test
    public void negativeRegisterTest() throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserData userData = new UserData(null, "a", "a");
        UserService userService = new UserService(userDAO, authDAO);
        Assertions.assertThrows(DataAccessException.class, ()->{userService.register(userData);});
    }
    @Test
    public void positiveRegisterTest() throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserData userData = new UserData("a", "a", "a");
        UserService userService = new UserService(userDAO, authDAO);
        var authData = userService.register(userData);
        var username = authDAO.getUsername(authData.authToken());
        Assertions.assertEquals(authData.username(), username);
    }

}
