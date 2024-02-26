package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData userData) throws DataAccessException {
        userDAO.createUser(userData);
        var authToken = authDAO.createAuth(userData.username());
        return new AuthData(authToken, userData.username());
    }

}
