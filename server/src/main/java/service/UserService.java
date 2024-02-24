package service;

import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    public AuthData register(UserData userData){
        return this.userDAO.register(userData);
    }
}
