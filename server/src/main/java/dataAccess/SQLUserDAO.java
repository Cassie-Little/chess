package dataAccess;

import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  userData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(30),
              `password` varchar(60),
              `email` varchar (30),
              PRIMARY KEY (`id`),
              INDEX(username)
            );
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }


    @Override
    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        if (
                userData.username() == null || userData.username().isBlank() ||
                        userData.password() == null || userData.password().isBlank() ||
                        userData.email() == null || userData.email().isBlank()) {
            throw new DataAccessException("Error: bad request");
        }
        try {
            getUser(userData.username());
            throw new DataAccessException("Error: already taken");
        }
        catch(DataAccessException ignored) {

        }
        var id = DatabaseManager.executeUpdate(statement, userData.username(), userData.password(), userData.email());

    }
    private UserData readUser(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email );
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username );
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Error: unknown username");
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE userData";
        DatabaseManager.executeUpdate(statement);
    }
}
