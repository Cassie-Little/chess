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
    public AuthData createGame(GameData gameData){
        return this.gameDAO.createGame(gameData);
    }
    public AuthData joinGame(GameData gameData) {
        return this.gameDAO.joinGame(gameData);
    }
}

