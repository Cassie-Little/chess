package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SessionService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;


    public SessionService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(UserData loginData) throws DataAccessException {
        try {
            var userData = userDAO.getUser(loginData.username());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(loginData.password(), userData.password())){
                throw new DataAccessException("Error: unauthorized");
            }
            var authToken = authDAO.createAuth(userData.username());
            return new AuthData(authToken, userData.username());
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: unauthorized");
        }

    }

    public void logout(String authToken) throws DataAccessException {
        authDAO.getUsername(authToken);
        authDAO.deleteAuth(authToken);
    }

}
