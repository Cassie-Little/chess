package serviceHandler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
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
    final AuthDAO authDAO;


    public GameResource(GameService gameService, Gson serializer, AuthDAO authDAO) {
        this.serializer = serializer;
        this.gameService = gameService;
        this.authDAO = authDAO;
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
        try {
            var authToken = request.headers("authorization");
            var games = this.gameService.listGames(authToken);
            return serializer.toJson(games);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
            } else {
                response.status(500);
            }
            return e.getMessage();
        }
    }

    private String createGameRequest(Request request, Response response) {
        try {
            var authToken = request.headers("authorization");
            var inputGameData = serializer.fromJson(request.body(), GameData.class);
            var gameData = this.gameService.createGame(authToken, inputGameData);
            return serializer.toJson(gameData);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
            } else if (e.getMessage().equals("Error: bad request")) {
                response.status(400);
            } else {
                response.status(500);
            }
            return e.getMessage();
        }
    }


    private String joinGameRequest(Request request, Response response) {
        try {
            var authToken = request.headers("authorization");
            var joinGameData = serializer.fromJson(request.body(), JoinGameData.class);
            this.gameService.joinGame(authToken, joinGameData);
            return "";
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
            } else if (e.getMessage().equals("Error: bad request")) {
                response.status(400);
            } else if (e.getMessage().equals("Error: already taken")) {
                response.status(403);
            } else {
                response.status(500);
            }
            return e.getMessage();
        }
    }

}
