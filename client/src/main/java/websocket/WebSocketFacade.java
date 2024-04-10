package websocket;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.GameError;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    Gson gson = new Gson();


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws exception.ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case ServerMessage.ServerMessageType.NOTIFICATION ->
                                notificationHandler.notify(gson.fromJson(message, Notification.class));
                        case ServerMessage.ServerMessageType.LOAD_GAME ->
                                notificationHandler.loadGame(gson.fromJson(message, LoadGame.class));
                        case ServerMessage.ServerMessageType.ERROR ->
                                notificationHandler.error(gson.fromJson(message, GameError.class));
                        default -> throw new IllegalStateException("Unexpected value: " + serverMessage);
                    }
                }
            });
        } catch (DeploymentException | URISyntaxException | IOException ex) {
            throw new exception.ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(JoinPlayer joinPlayer) throws exception.ResponseException {
        try {
            this.session.getBasicRemote().sendText(gson.toJson(joinPlayer));
        } catch (IOException ex) {
            throw new exception.ResponseException(500, ex.getMessage());
        }
    }
    public void joinObserver(JoinObserver joinObserver) throws exception.ResponseException {
        try {
            this.session.getBasicRemote().sendText(gson.toJson(joinObserver));
        } catch (IOException ex) {
            throw new exception.ResponseException(500, ex.getMessage());
        }
    }


}

