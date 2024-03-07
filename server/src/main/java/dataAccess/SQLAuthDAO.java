package dataAccess;

import model.AuthData;

import java.sql.*;
import java.util.UUID;

import static dataAccess.DatabaseManager.createAuthTable;
import static dataAccess.DatabaseManager.createDatabase;
import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class SQLAuthDAO implements AuthDAO{
    public  SQLAuthDAO() throws DataAccessException {
        createDatabase();
        createAuthTable();
    }


    public int insertData(Connection conn, AuthData authData) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO authData (authToken, username) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, authData.authToken());
            preparedStatement.setString(2, authData.username());

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
    public String createAuth(Connection conn, String username) throws SQLException {
        var newAuthToken = UUID.randomUUID().toString();
        insertData(conn, newAuthToken, username);
        return newAuthToken;
    }

    @Override
    public String getUsername(Connection conn, String authToken) throws DataAccessException, SQLException {
        try (var preparedStatement = conn.prepareStatement("SELECT username FROM authData WHERE authToken=?")) {
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var username = rs.getString("username");

                    System.out.printf("username: %s", username);
                }
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void deleteAuth(Connection conn, String authToken) throws SQLException {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authData WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }

    @Override
    public void clear(Connection conn) {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM authData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
