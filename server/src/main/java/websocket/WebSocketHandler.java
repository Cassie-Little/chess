package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson;
    private final GameService gameService;


    public WebSocketHandler(Gson gson, GameService gameService) {

        this.gson = gson;
        this.gameService = gameService;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType() ){
            case UserGameCommand.CommandType.JOIN_PLAYER -> this.joinPlayer(session, message);
//            case JOIN_OBSERVER -> joinObserver(userGameCommand.getAuthString(), session);
//            case  MAKE_MOVE -> makeMove(userGameCommand.getAuthString(), session);
//            case LEAVE -> leave(userGameCommand.getAuthString());
//            case RESIGN -> resign(userGameCommand.getAuthString());
            default -> throw new DataAccessException("default");
        }
    }

    public void joinPlayer(Session session, String message) throws IOException, DataAccessException {
        var joinPlayer = gson.fromJson(message, JoinPlayer.class);
        var gameData = gameService.getGame(joinPlayer.getAuthString(), joinPlayer.getGameID());
        var player = joinPlayer.getPlayerColor().equals(ChessGame.TeamColor.WHITE) ? gameData.whiteUsername():gameData.blackUsername();
        connections.add(player, session);
        var loadGame = new LoadGame(gameData);
        var loadGameJson  = gson.toJson(loadGame);
        session.getRemote().sendString(loadGameJson);
        var notification = new Notification(String.format("%s is joining as %s", player, joinPlayer.getPlayerColor()));
        connections.broadcast(player, notification);
    }

//    public void joinObserver(String authoken, Session session) throws IOException, DataAccessException {
//        String visitorName = authDAO.getUsername(authoken);
//        connections.add(visitorName, session);
//        var message = String.format("%s is joining as an observer", visitorName);
//        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
//        connections.broadcast(visitorName, serverMessage);
//    }
//
//    public void makeMove(String authoken, Session session) throws IOException, DataAccessException {
//        String visitorName = authDAO.getUsername(authoken);
//        //make sure its a valid move
//        //update game
//        connections.add(visitorName, session);
//        var message = String.format("%s move their %s to position %s", visitorName, ChessPiece.PieceType.PAWN, "a4");
//        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
//        connections.broadcast(visitorName, serverMessage);
//    }
//    public void leave(String authoken) throws IOException, DataAccessException {
//        String visitorName = authDAO.getUsername(authoken);
//        connections.remove(visitorName);
//        //update game database
//        var message = String.format("%s left the game", visitorName);
//        //var notification = new ServerMessage(ServerMessage.Type.DEPARTURE, message);
//        //connections.broadcast(visitorName, message);
//    }
//
//    public void resign(String authoken) throws IOException, DataAccessException {
//        String visitorName = authDAO.getUsername(authoken);
//        connections.remove(visitorName);
//        //update gme database
//        var message = String.format("%s resigned", visitorName);
//        //var notification = new ServerMessage(ServerMessage.Type.DEPARTURE, message);
//        //connections.broadcast(visitorName, message);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new ServerMessage(ServerMessage.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}
