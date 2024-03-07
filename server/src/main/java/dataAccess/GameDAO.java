package dataAccess;

import model.GameData;
import model.GameListData;

import java.sql.Connection;

public interface GameDAO {
    //void clear();
    GameListData listGames();

    int createGame(String gameName) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void clear(Connection conn);
}

