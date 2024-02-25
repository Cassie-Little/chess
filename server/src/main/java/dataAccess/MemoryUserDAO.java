package dataAccess;

import model.UserData;
import model.AuthData;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> userDB;

    public MemoryUserDAO() {
        this.userDB = new HashMap<>();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (
                userData.username() == null || userData.username().isBlank() ||
                userData.password() == null || userData.password().isBlank() ||
                userData.email() == null || userData.email().isBlank()) {
            throw new DataAccessException("Error: bad request");
        }
        if (userDB.containsKey(userData.username())) {
            throw new DataAccessException("Error: already taken");
        }
        this.userDB.put(userData.username(), userData);

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (userDB.containsKey(username)){
            return this.userDB.get(username);
        }
        throw new DataAccessException("Error: unknown username");
    }
}
