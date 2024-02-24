package dataAccess;

import model.AuthData;
import model.GameData;

public interface GameDAO {
    void clear();
    AuthData listGames(GameData gameData);

    AuthData createGame(GameData gameData);

    AuthData joinGame(GameData gameData);
}

