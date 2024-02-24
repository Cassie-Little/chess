package dataAccess;
import model.GameData;
import model.AuthData;

public class MemoryGameDAO implements GameDAO {

    @Override
    public void clear() {
       System.out.print("hello from memory game dao");
    }
    public AuthData listGames(GameData gameData){
        System.out.printf("games: [ gameID: %s, whiteUsername: %s, blackUsername: %s, gameName: %s]",
                gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
        return null;
    }
    public AuthData createGame(GameData gameData) {
        System.out.printf("gameID: %s", gameData.gameID());
        return null;
    }
    @Override
    public AuthData joinGame(GameData gameData) {
        System.out.print("joined game");
        return null;
    }

}
