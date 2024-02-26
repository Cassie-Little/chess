package dataAccess;

import model.UserData;
import model.AuthData;

public interface UserDAO {
    public void createUser(UserData userData) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;

    void clear();

    void logout(String authToken);
}
