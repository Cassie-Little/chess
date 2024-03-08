package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SQLAuthDAOTests {
    @Test
    public void createAuthPositiveTest() throws DataAccessException{
        var authDAO = new SQLAuthDAO();
        var authToken = authDAO.createAuth("mii");
        Assertions.assertTrue(authToken.length() >= 0);
    }

    @Test
    public void createAuthNegativeTest() throws DataAccessException{
        var authDAO = new SQLAuthDAO();
        var authToken = authDAO.createAuth("mii");
    }
    @Test
    public void getUsernamePositiveTest() throws DataAccessException{
        var authDAO = new SQLAuthDAO();
        var authToken = authDAO.createAuth("mii");
        var user = authDAO.getUsername(authToken);
        Assertions.assertNotNull(user);
        Assertions.assertEquals("mii", user);
    }
    @Test
    public void getUsernameNegativeTest() throws DataAccessException{
        var authDAO = new SQLAuthDAO();
        var authToken = authDAO.createAuth("mii");
        Assertions.assertThrows(DataAccessException.class, ()->{authDAO.getUsername("oops");});
    }

    @Test
    public void deleteAuthPositiveTest() throws DataAccessException{
        var authDAO = new SQLAuthDAO();
        var authToken = authDAO.createAuth("mii");
        authDAO.deleteAuth(authToken);
        Assertions.assertThrows(DataAccessException.class, ()->{authDAO.getUsername(authToken);});
    }
    @Test
    public void clearTest() throws DataAccessException{
        var authDAO = new SQLAuthDAO();
        var authToken = authDAO.createAuth("mii");
        authDAO.clear();
        Assertions.assertThrows(DataAccessException.class, ()->{authDAO.getUsername(authToken);});
    }

}
