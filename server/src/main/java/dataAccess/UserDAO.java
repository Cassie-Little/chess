package dataAccess;

import model.UserData;
import model.AuthData;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear();

}
