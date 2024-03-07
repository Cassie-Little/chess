package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public interface AuthDAO {
//public String createAuth(String username);
//public  String getUsername(String authToken) throws DataAccessException;
//public void deleteAuth(String authToken);

    String createAuth(Connection conn, String username) throws SQLException;

    String getUsername(Connection conn, String authToken) throws DataAccessException, SQLException;

    void deleteAuth(Connection conn, String authToken) throws SQLException;

    //void clear();

    void clear(Connection conn);
}
