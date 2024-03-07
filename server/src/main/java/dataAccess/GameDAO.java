package dataAccess;

import model.GameData;
import model.GameListData;

public interface GameDAO {
    //void clear();
    GameListData listGames() throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void clear() throws DataAccessException;
}

