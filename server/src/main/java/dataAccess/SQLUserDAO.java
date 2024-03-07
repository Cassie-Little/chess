package dataAccess;

import model.UserData;

import static dataAccess.DatabaseManager.createDatabase;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws DataAccessException {
        createDatabase();
        //createUserTable();
    }


    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }
}
