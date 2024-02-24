package dataAccess;

import model.AuthData;
import model.GameData;

public interface GameDAO {
    void clear();
    AuthData listGames(GameData gameData);
}

