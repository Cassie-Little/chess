package dataAccess;
import model.GameData;
import model.GameListData;
import model.JoinGameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> gameDB;
    public MemoryGameDAO() {
        this.gameDB = new HashMap<>();
    }

    @Override
    public void clear() {
        gameDB.clear();
    }

    @Override
    public GameListData listGames() {
        return null;
    }

    @Override
    public int createGame(String gameName, GameData gameData) {
        return 0;
    }

    @Override
    public void joinGame(String authToken, JoinGameData gameData) {
    }

    @Override
    public GameData getGame(int gameID) {
        if (gameDB.containsKey(gameID)){
            return this.gameDB.get(gameID);
        }
        return null;
    }
}
