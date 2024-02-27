package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

public class ClearTests {
    @Test
    public void positiveClear() throws Exception{
        AuthDAO authDao = new MemoryAuthDAO();
        UserData person = new UserData("username",null, null);


    }
}
