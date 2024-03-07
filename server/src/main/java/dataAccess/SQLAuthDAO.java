package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO{
    public  SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    public int insertData(Connection conn, String authToken, String username) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO authData (authToken, username) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();
            var ID = 0;
            if (resultSet.next()) {
                ID = resultSet.getInt(1);
            }

            return ID;
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        var newAuthToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        var id = executeUpdate(statement, newAuthToken, username);
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
        executeUpdate(statement, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE pet";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(60),
              `username` varchar(30),
              PRIMARY KEY (`id`),
              INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
