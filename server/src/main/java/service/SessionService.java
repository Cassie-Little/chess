package service;

import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class SessionService {
    private final UserDAO userDAO;


    public SessionService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData login(UserData userData){
        return this.userDAO.login(userData);
    }
    public AuthData logout(){
        return this.userDAO.logout();
    }

}
