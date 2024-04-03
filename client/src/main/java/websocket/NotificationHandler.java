package websocket;

import webSocketMessages.serverMessages.GameError;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;

public interface NotificationHandler {
    void notify(Notification notification);

    void loadGame(LoadGame loadGame);
    void error(GameError gameError);
}
