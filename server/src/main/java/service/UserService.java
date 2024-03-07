package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        var newUserData = hashPassword(userData);
        userDAO.createUser(newUserData);
        var authToken = authDAO.createAuth(newUserData.username());
        return new AuthData(authToken, newUserData.username());
    }
    private UserData hashPassword(UserData userData) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(userData.password());
        return new UserData(userData.username(), hash, userData.email());
    }

}
