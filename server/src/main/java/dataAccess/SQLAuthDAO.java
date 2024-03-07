package dataAccess;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    public  SQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  authData (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(60),
              `username` varchar(30),
              PRIMARY KEY (`id`),
              INDEX(authToken)
            );
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        var newAuthToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        var id = DatabaseManager.executeUpdate(statement, newAuthToken, username);
        return newAuthToken;
    }

    @Override
    public String getUsername( String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM authData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken );
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("username");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void deleteAuth( String authToken) throws DataAccessException {
        var statement = "DELETE FROM authData WHERE authToken=?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE authData";
        DatabaseManager.executeUpdate(statement);
    }


}
