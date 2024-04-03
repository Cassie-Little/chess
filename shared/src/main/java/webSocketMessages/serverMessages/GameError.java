package webSocketMessages.serverMessages;

public class GameError extends ServerMessage {

    private final String errorMessage;

    public GameError(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
