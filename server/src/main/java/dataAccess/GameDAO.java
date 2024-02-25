package dataAccess;

import model.GameData;
import model.GameListData;
import model.JoinGameData;

public interface GameDAO {
    void clear();
    GameListData listGames();

    int createGame(String gameName);

    void updateGame(int gameID, GameData gameData);

    GameData getGame(int gameID);

}

