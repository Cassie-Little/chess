package service;

import chess.ChessBoard;
import chess.ChessGame;
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

    public GameService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void clear() throws DataAccessException {
        this.gameDAO.clear();
        this.authDAO.clear();
        this.userDAO.clear();
    }

    public GameListData listGames(String authToken) throws DataAccessException {
        authDAO.getUsername(authToken);
        return this.gameDAO.listGames();
    }

    public int createGame(String authToken, GameData gameData) throws DataAccessException {
        authDAO.getUsername(authToken);
        var game = new ChessGame();
        var board = new ChessBoard();
        board.resetBoard();
        game.setBoard(board);
        GameData gameData1 = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        return this.gameDAO.createGame(gameData1);
    }

    public void joinGame(String authToken, JoinGameData joinGameData) throws DataAccessException {
        var username = authDAO.getUsername(authToken);
        var game = this.gameDAO.getGame(joinGameData.gameID());
        if (joinGameData.playerColor() == null) {
            joinAsWatcher();
            return;
        } else if (joinGameData.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null && !game.whiteUsername().equals(username)) {
                throw new DataAccessException("Error: already taken");
            }
            var gameData = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(gameData);
        } else if (joinGameData.playerColor().equals("BLACK")) {
            if (game.blackUsername() != null && !game.blackUsername().equals(username)) {
                throw new DataAccessException("Error: already taken");
            }
            var gameData = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            gameDAO.updateGame(gameData);
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }

    public void updateGame(String authToken, GameData gameData) throws DataAccessException {
        authDAO.getUsername(authToken);
        gameDAO.updateGame(gameData);
    }

    public GameData getGame(String authToken, int gameID) throws DataAccessException {
        authDAO.getUsername(authToken);
        return gameDAO.getGame(gameID);
    }

    public void joinAsWatcher() {

    }

}

