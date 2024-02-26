package dataAccess;

public interface AuthDAO {
public String createAuth(String username);
public  String getUsername(String authToken) throws DataAccessException;
public void deleteAuth(String authToken);
void clear();
}
