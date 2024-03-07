package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

import static dataAccess.DatabaseManager.createDatabase;
import static dataAccess.DatabaseManager.createUserTable;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws DataAccessException {
        createDatabase();
        createUserTable();
    }


    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear(Connection conn) {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM userData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
