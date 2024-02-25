package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.GameListData;
import model.JoinGameData;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
    }
    public void clear(){
        this.gameDAO.clear();
        this.authDAO.clear();
        this.userDAO.clear();
    }
    public GameListData listGames(String authToken){
       return this.gameDAO.listGames(authToken);
    }
    public GameData createGame(String authToken, GameData gameData){
        return this.gameDAO.createGame(authToken, gameData);
    }
    public void joinGame( String authToken, JoinGameData joinGameData) {
        this.gameDAO.joinGame(authToken, joinGameData);
    }
}

