package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import model.GameListData;
import model.JoinGameData;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public GameService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public void clear(){
        this.gameDAO.clear();
        this.authDAO.clear();
        this.userDAO.clear();
    }
    public GameListData listGames(){
       return this.gameDAO.listGames();
    }
    public int createGame(String authToken, GameData gameData){
        this.gameDAO.createGame(authToken, gameData);
        return gameData.gameID();
    }

    public void joinGame( String authToken, JoinGameData joinGameData) {
        this.gameDAO.joinGame( authToken, joinGameData);
    }
}

