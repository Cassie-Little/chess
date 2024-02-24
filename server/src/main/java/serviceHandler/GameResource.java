package serviceHandler;

import com.google.gson.Gson;
import dataAccess.GameDAO;
import model.GameData;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class GameResource {
    //final GameDAO gameDAO;
    final Gson serializer;
    final GameService gameService;



    public GameResource(GameService gameService,  Gson serializer) {
        this.serializer = serializer;
        this.gameService = gameService;
    }
    public void listGamesRoutes(){
        Spark.get("/game", this::listGamesRequest);
    }
    private String listGamesRequest(Request request, Response response){
        var gameData = serializer.fromJson(response.body(), GameData.class);
       // var authData = this.gameService.listGames(gameData);
        return serializer.toJson(gameData);
    }
}
