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
        userDAO.createUser(userData);
        var authToken = authDAO.createAuth(userData.username());
        return new AuthData(authToken, userData.username());
    }
    public String hashPassword(UserData userData) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.(userData.password());
        return hash;
    }

}
