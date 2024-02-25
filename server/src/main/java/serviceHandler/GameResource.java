package serviceHandler;

import com.google.gson.Gson;
import dataAccess.GameDAO;
import model.GameData;
import model.JoinGameData;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class GameResource {
    //final GameDAO gameDAO;
    final Gson serializer;
    final GameService gameService;


    public GameResource(GameService gameService, Gson serializer) {
        this.serializer = serializer;
        this.gameService = gameService;
    }

    public void registerRoutes() {
        Spark.get("/game", this::listGamesRequest);
        Spark.delete("/db", this::clearRequest);
        Spark.post("/game", this::createGameRequest);
        Spark.put("/game", this::joinGameRequest);
    }

    private String clearRequest(Request request, Response response) {
        this.gameService.clear();
        return "";
    }

    private String listGamesRequest(Request request, Response response) {
        var authToken = request.queryParams("authorization");
        var games = this.gameService.listGames(authToken);
        return serializer.toJson(games);
    }


    private String createGameRequest(Request request, Response response) {
        var authToken = request.queryParams("authorization");
        var inputGameData = serializer.fromJson(request.body(), GameData.class);
        var gameData = this.gameService.createGame(authToken, inputGameData);
        return serializer.toJson(gameData);
    }

    private String joinGameRequest(Request request, Response response) {
        var authToken = request.queryParams("authorization");
        var joinGameData = serializer.fromJson(request.body(), JoinGameData.class);
        this.gameService.joinGame(authToken, joinGameData);
        return "";
    }
}
