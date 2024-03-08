package dataAccessTests;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SQLGameDAOTests {
    @Test
    public void createGamePositiveTest() throws DataAccessException {
        var gameDAO = new SQLGameDAO();
        var gameID = gameDAO.createGame("NEW GAME");
        Assertions.assertTrue(gameID >= 0);
    }

    @Test
    public void createGameNegativeTest() throws DataAccessException {
        var gameDAO = new SQLGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(null);
        });
    }

    @Test
    public void getGamePositiveTest() throws DataAccessException {
        var gameDAO = new SQLGameDAO();
        var gameID = gameDAO.createGame("NEW GAME");
        var game = gameDAO.getGame(gameID);
        Assertions.assertNotNull(game);
        Assertions.assertEquals("NEW GAME", game.gameName());
    }

    @Test
    public void getGameNegativeTest() throws DataAccessException {
        var gameDAO = new SQLGameDAO();
        int gameID = -53;
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(gameID);
        });
    }

    @Test
    public void updateGamePositiveTest() throws DataAccessException {
        var gameDAO = new SQLGameDAO();
        var gameID = gameDAO.createGame("NEW GAME");
        var gameData = gameDAO.getGame(gameID);
        var newGame = new ChessGame();
        var board = new ChessBoard();
        board.resetBoard();
        newGame.setBoard(board);
        var updatedGame = new GameData(gameID, "bob", "george", gameData.gameName(), newGame);
        gameDAO.updateGame(updatedGame);
        var gameDataAfterUpdate = gameDAO.getGame(gameID);
        Assertions.assertEquals("bob", gameDataAfterUpdate.whiteUsername());
        Assertions.assertEquals("george", gameDataAfterUpdate.blackUsername());
        Assertions.assertEquals("NEW GAME", gameDataAfterUpdate.gameName());
        Assertions.assertNotNull(gameDataAfterUpdate.game());
        var boardAfterUpdate = gameDataAfterUpdate.game().getBoard();
        var numWhitePawns = boardAfterUpdate.getPiecePositions(ChessPiece.PieceType.PAWN, ChessGame.TeamColor.WHITE).size();
        Assertions.assertEquals(8, numWhitePawns);
    }

    @Test
    public void updateGameNegativeTest() throws DataAccessException {
        var gameDAO = new SQLGameDAO();
        var gameID = gameDAO.createGame("NEW GAME");
        var gameData = gameDAO.getGame(gameID);
        var updatedGame = new GameData(gameID, "bob", "george", "Invalid Game Name", new ChessGame()); // Invalid game name
        gameDAO.updateGame(updatedGame);
        var gameDataAfterUpdate = gameDAO.getGame(gameID);
        Assertions.assertNotEquals(gameData, gameDataAfterUpdate);
    }

    @Test
    public void clearTest() throws DataAccessException {
        var gameDAO = new SQLGameDAO();
        var gameID = gameDAO.createGame("NEW GAME");
        var gameData = gameDAO.getGame(gameID);
        var newGame = new ChessGame();
        var board = new ChessBoard();
        board.resetBoard();
        newGame.setBoard(board);
        var updatedGame = new GameData(gameID, "bob", "george", gameData.gameName(), newGame);
        gameDAO.updateGame(updatedGame);
        var gameDataAfterUpdate = gameDAO.getGame(gameID);
        gameDAO.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(gameID);
        });

    }

}
