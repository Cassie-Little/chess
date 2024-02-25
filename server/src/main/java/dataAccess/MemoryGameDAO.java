package dataAccess;
import model.GameData;
import model.AuthData;
import model.GameListData;
import model.JoinGameData;

import java.util.HashMap;
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
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) {

    }

    @Override
    public GameData getGame(int gameID) {
        if (gameDB.containsKey(gameID)){
            return this.gameDB.get(gameID);
        }
        return null;
    }
}
