package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {

    private final ChessGame game;
    private final String blackPlayer;
    private final String whitePlayer;

    private final String gameName;

    public LoadGame(ChessGame game, String blackPlayer, String whitePlayer, String gameName) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.gameName = gameName;
    }

    public ChessGame game() {
        return game;
    }

    public String getBlackPlayer(){
        return blackPlayer;
    }
    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getGameName() {
        return gameName;
    }
}
