package service;

import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
    public AuthData listGames(GameData gameData){
       return this.gameDAO.listGames(gameData);
    }
}
