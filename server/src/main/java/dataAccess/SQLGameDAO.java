package dataAccess;

import com.google.gson.Gson;
import model.GameData;
import model.GameListData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  gameData (
              `id` int NOT NULL AUTO_INCREMENT,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            );
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE gameData";
        DatabaseManager.executeUpdate(statement);
    }


    @Override
    public GameListData listGames() throws DataAccessException {
        var result = new GameListData(new ArrayList<GameData>());
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.games().add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }
    private GameData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var json = rs.getString("json");
        var gameData = new Gson().fromJson(json, GameData.class);
        return new GameData(id, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
    }



    @Override
    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.isBlank()) {
            throw new DataAccessException("Error: bad request");
        }
        var json = new Gson().toJson(new GameData(0, null, null, gameName, null));
        var statement = "INSERT INTO gameData (id, json) VALUES(?, ?)";
        var id = DatabaseManager.executeUpdate(statement, json);
        return id;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var check = "SELECT 1 FROM gameData IF EXISTS WHERE id = ?";
        if (DatabaseManager.executeUpdate(check) == 1) {
            var json = new Gson().toJson(gameData);
            var statement = "UPDATE gameData SET json = ? WHERE id = ?";
            DatabaseManager.executeUpdate(statement, json, gameData.gameID());
        }
        throw new DataAccessException("Error: bad request");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM gameData WHERE id = ?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                         return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Error: bad request");
    }


}

