package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.GameError;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson;
    private final GameService gameService;
    private final UserService userService;


    public WebSocketHandler(Gson gson, GameService gameService, UserService userService) {

        this.gson = gson;
        this.gameService = gameService;
        this.userService = userService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        try {
            switch (userGameCommand.getCommandType()) {
                case UserGameCommand.CommandType.JOIN_PLAYER -> this.joinPlayer(session, message);
                case UserGameCommand.CommandType.JOIN_OBSERVER -> this.joinObserver(session, message);
                case UserGameCommand.CommandType.MAKE_MOVE -> this.makeMove(session, message);
                case UserGameCommand.CommandType.LEAVE -> this.leave(session, message);
                case UserGameCommand.CommandType.RESIGN -> this.resign(session, message);
                default -> throw new DataAccessException("default");
            }
        } catch (Exception e) {
            var errorJson = gson.toJson(new GameError("Error: " + e.getMessage()));
            session.getRemote().sendString(errorJson);
        }
    }

    public void joinPlayer(Session session, String message) throws IOException, DataAccessException {
        if (message.length() > 1) {
            var joinPlayer = gson.fromJson(message, JoinPlayer.class);
            var gameData = gameService.getGame(joinPlayer.getAuthString(), joinPlayer.getGameID());
            var username = userService.getUser(joinPlayer.getAuthString());
            var player = joinPlayer.getPlayerColor().equals(ChessGame.TeamColor.WHITE) ? gameData.whiteUsername() : gameData.blackUsername();
            if (!username.equals(player)) {
                throw new DataAccessException("Error: " + username + " called joinPlayer with wrong team color");
            }
            connections.add(player, session, gameData.gameID());
            sendLoadGame(session, gameData);
            broadcastNotification(player,gameData.gameID(), String.format("%s is joining as %s", player, joinPlayer.getPlayerColor()));
        } else {
            throw new DataAccessException("bad message");
        }
    }

    private void broadcastNotification(String player, int gameID, String message) throws IOException {
        var notification = new Notification(message);
        var notificationJson = gson.toJson(notification);
        connections.broadcast(player, gameID, notificationJson);
    }

    private void sendLoadGame(Session session, GameData gameData) throws IOException {
        var loadGame = new LoadGame(gameData.game(), gameData.blackUsername(), gameData.whiteUsername(), gameData.gameName());
        var loadGameJson = gson.toJson(loadGame);
        session.getRemote().sendString(loadGameJson);
    }
    private void broadcastLoadGame(GameData gameData) throws IOException {
        var loadGame = new LoadGame(gameData.game(), gameData.blackUsername(), gameData.whiteUsername(), gameData.gameName());
        var loadGameJson = gson.toJson(loadGame);
        connections.broadcast("", gameData.gameID(), loadGameJson);
    }

    public void joinObserver(Session session, String message) throws IOException, DataAccessException {
        if (message.length() > 1) {
            var joinObserver = gson.fromJson(message, JoinObserver.class);
            var gameData = gameService.getGame(joinObserver.getAuthString(), joinObserver.getGameID());
            var player = userService.getUser(joinObserver.getAuthString());
            connections.add(player, session, gameData.gameID());
            sendLoadGame(session, gameData);
            broadcastNotification(player, gameData.gameID(), String.format("%s is joining an observer", player));
        } else {
            throw new DataAccessException("bad message");
        }
    }

    public void makeMove(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
        if (message.isEmpty()) {
            throw new DataAccessException("bad message");
        }
        var makeMove = gson.fromJson(message, MakeMove.class);
        var gameData = gameService.getGame(makeMove.getAuthString(), makeMove.getGameID());
        var username = userService.getUser(makeMove.getAuthString());
        var teamColor = getTeamColorUser(username, gameData);
        var piece = gameData.game().getBoard().getPiece(makeMove.getMove().getStartPosition());
        if (!piece.getTeamColor().equals(teamColor)){
            throw new DataAccessException("wrong team color");
        }
        if (!teamColor.equals(gameData.game().getTeamTurn())) {
            throw new DataAccessException("wrong team");
        }
        gameData.game().makeMove(makeMove.getMove());
        gameService.updateGame(makeMove.getAuthString(), gameData);
        connections.add(username, session, gameData.gameID());
        broadcastLoadGame(gameData);
        broadcastNotification(username, gameData.gameID(), String.format("%s move their %s to position %s", username, piece, makeMove.getMove().getEndPosition()));
    }

    private ChessGame.TeamColor getTeamColorUser(String username, GameData gameData) throws DataAccessException {
        if (username.equals(gameData.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        throw new DataAccessException("username does not match color");
    }

    public void leave(Session session, String message) throws IOException, DataAccessException {
        var leave = gson.fromJson(message, Leave.class);
        var username = userService.getUser(leave.getAuthString());
        connections.remove(username, leave.getGameID());
        var gameData = gameService.getGame(leave.getAuthString(), leave.getGameID());
        gameService.updateGame(leave.getAuthString(), gameData);
        broadcastNotification(username, leave.getGameID(), String.format("%s left the game", username));
    }

    public void resign(Session session, String message) throws IOException, DataAccessException {
        var resign = gson.fromJson(message, Resign.class);
        var username = userService.getUser(resign.getAuthString());
        var gameData = gameService.getGame(resign.getAuthString(), resign.getGameID());
        if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
            throw new DataAccessException( username + " cannot resign");
        }
        if (gameData.game().getTeamTurn() == ChessGame.TeamColor.NONE){
            throw new DataAccessException("you already resigned");
        }
        gameData.game().setTeamTurn(ChessGame.TeamColor.NONE);
        gameService.updateGame(resign.getAuthString(), gameData);
        broadcastNotification("", gameData.gameID(), String.format("%s resigned from the game", username));

    }

}
