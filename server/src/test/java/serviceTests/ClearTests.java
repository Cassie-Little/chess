package serviceTests;

import dataAccess.*;
import model.*;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

public class ClearTests {
    @Test
    public void positiveClear() throws Exception{
        GameDAO gameDAO = new MemoryGameDAO();
        GameData game = new GameData(1,"white user", "black user", "name", null);
        ArrayList<GameData> games = null;
        games.add(game);
        UserData person = new UserData("username","password", "email");
        AuthData token = new AuthData("token", "username");
        JoinGameData joinedGame = new JoinGameData("WHITE",1 );
        GameListData listOfGames = new GameListData(games);



    }
}
