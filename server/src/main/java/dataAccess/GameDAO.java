package dataAccess;

import model.GameData;
import model.GameListData;
import model.JoinGameData;

public interface GameDAO {
    void clear();
    GameListData listGames();

    int createGame(String gameName, GameData gameData);

    void joinGame(String authToken, JoinGameData gameData);

    GameData getGame(int gameID);

}

