package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

public class ClearTests {
    @Test
    public void positiveClear() throws Exception{
        AuthDAO authDao = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        //var newUser = UserService.register();
    }
}
