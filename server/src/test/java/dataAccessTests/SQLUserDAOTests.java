package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SQLUserDAOTests {
    @Test
    public void createUserPositiveTest() throws DataAccessException{
        var userDAO = new SQLUserDAO();
        var userData = new UserData("username", "password", "e@mail");
        userDAO.createUser(userData);
    }

    @Test
    public void createUserNegativeTest() throws DataAccessException{
        var userDAO = new SQLUserDAO();
        var userData = new UserData("username1", "password1", "e@mail1");
        userDAO.createUser(userData);
        var userDataCopiedUsername = new UserData("username1", "password123", "email");
        Assertions.assertThrows(DataAccessException.class, ()->{userDAO.createUser(userDataCopiedUsername);});
    }

    @Test
    public void getUserPositiveTest() throws DataAccessException {
        var userDAO = new SQLUserDAO();
        var userData = new UserData("username!", "password!", "e@mail!");
        userDAO.createUser(userData);
        var user = userDAO.getUser(userData.username());
        Assertions.assertNotNull(user);

    }
    @Test
    public void getUserNegativeTest() throws DataAccessException {
        var userDAO = new SQLUserDAO();
        var userData = new UserData("username!", "password!", "e@mail!");
        Assertions.assertThrows(DataAccessException.class, ()->{userDAO.getUser("i don't exist");});
    }

    @Test
    public void clearTest() throws DataAccessException{
        var userDAO = new SQLUserDAO();
        var userData = new UserData("username!", "password!", "e@mail!");
        userDAO.clear();
        Assertions.assertThrows(DataAccessException.class, ()->{userDAO.getUser(userData.username());});
    }
}
