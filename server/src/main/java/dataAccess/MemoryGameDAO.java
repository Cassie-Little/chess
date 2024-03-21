package dataAccess;

import model.GameData;
import model.GameListData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> gameDB;
    private int id = 0;

    public MemoryGameDAO() {
        this.gameDB = new HashMap<>();
    }

    @Override
    public void clear() {
        gameDB.clear();
    }

    @Override
    public GameListData listGames() {
        var gamesList = new GameListData(new ArrayList<>());
        for (int gameID : gameDB.keySet()) {
            var gameData = gameDB.get(gameID);
            gamesList.games().add(gameData);
        }
        return gamesList;
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        if (gameData == null || gameData.gameName() == null || gameData.gameName().isBlank()) {
            throw new DataAccessException("Error: bad request");
        }
        id++;
        gameDB.put(id, gameData);
        return id;
    }


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if (gameDB.containsKey(gameID)) {
            return this.gameDB.get(gameID);
        }
        throw new DataAccessException("Error: bad request");
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        if (gameDB.containsKey(gameData.gameID())) {
            gameDB.put(gameData.gameID(), gameData);
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }
}
