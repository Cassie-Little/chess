package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.SessionService;
import service.UserService;
import serviceHandler.SessionResource;

public class LogoutTests {
    @Test
    public void positiveLogoutTest() throws Exception {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        UserData userData = new UserData("a", "a", "a");
        UserService userService = new UserService(userDAO, authDAO);
        SessionService sessionService = new SessionService(userDAO, authDAO);
        var authData = userService.register(userData);
        var authToken = authData.authToken();
        sessionService.logout(authToken);
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getUsername(authToken);
        });
    }

    @Test
    public void negativeLogoutTest() throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        AuthData authData = new AuthData("token", "username");
        SessionService sessionService = new SessionService(userDAO, authDAO);
        String fakeAuthToken = "gotcha!";
        Assertions.assertThrows(DataAccessException.class, () -> {
            sessionService.logout(fakeAuthToken);
        });
    }
}
