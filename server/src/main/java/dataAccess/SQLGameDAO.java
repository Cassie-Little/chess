package dataAccess;import com.google.gson.Gson;
import model.GameData;
import model.GameListData;

import java.sql.Connection;
import java.sql.SQLException;

import static dataAccess.DatabaseManager.createGameTable;
import static dataAccess.DatabaseManager.createDatabase;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        createDatabase();
        createGameTable();
    }

    public int insertData(Connection conn, GameData gameData) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, gameData.gameID());
            preparedStatement.setString(2, gameData.whiteUsername());
            preparedStatement.setString(3, gameData.blackUsername());
            preparedStatement.setString(4, gameData.gameName());
            var game = new Gson().toJson(gameData.game());
            preparedStatement.setString(5, game);

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
    public void clear(Connection conn) {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM GameData")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

@Override
public GameListData listGames() {
    return null;
}

@Override
public int createGame(String gameName) throws DataAccessException {
    return 0;
}

@Override
public void updateGame(GameData gameData) throws DataAccessException {

}

@Override
public GameData getGame(int gameID) throws DataAccessException {
    return null;
}
}
