package dataAccess;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private final Map<String, String> authDB;
    public MemoryAuthDAO(){
        this.authDB = new HashMap<>();
    }
    public String createAuth(String username) {
        var newAuthToken =  UUID.randomUUID().toString();
        this.authDB.put(newAuthToken, username);
        return newAuthToken;
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        if(authDB.containsKey(authToken)){
            return this.authDB.get(authToken);
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void deleteAuth(String authToken) {
        if(authDB.containsKey(authToken)){
           this.authDB.remove(authToken);
        }
    }
    public void clear(){
        authDB.clear();
    }


}
