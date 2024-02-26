package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
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
    public GameListData listGames(String authToken) throws DataAccessException{
       authDAO.getUsername(authToken);
        return this.gameDAO.listGames();
    }
    public int createGame(String authToken, GameData gameData) throws DataAccessException {
        authDAO.getUsername(authToken);
        return this.gameDAO.createGame(gameData.gameName());
    }

    public void joinGame( String authToken, JoinGameData joinGameData)  throws DataAccessException{
        var username= authDAO.getUsername(authToken);
        var game = this.gameDAO.getGame(joinGameData.gameID());
        if (joinGameData.playerColor() == null){
            joinAsWatcher();
            return;
        }
        else if (joinGameData.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null){
                throw  new DataAccessException("Error: already taken");
            }
            var gameData = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(gameData);
        }
        else if (joinGameData.playerColor().equals("BLACK")) {
            if (game.blackUsername() != null){
                throw  new DataAccessException("Error: already taken");
            }
            var gameData = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            gameDAO.updateGame(gameData);
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }
    public void joinAsWatcher(){

    }

}

