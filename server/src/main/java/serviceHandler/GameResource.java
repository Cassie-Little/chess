package serviceHandler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.CreateGameResponse;
import model.GameData;
import model.JoinGameData;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class GameResource {
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
        Spark.patch("/game", this::updateGame);
    }


    private String clearRequest(Request request, Response response) throws DataAccessException {
        this.gameService.clear();
        return "";
    }

    private String listGamesRequest(Request request, Response response) {
        try {
            var authToken = request.headers("authorization");
            var gameID = request.queryParams("gameID");
            if (gameID == null) {
                var games = this.gameService.listGames(authToken);
                return serializer.toJson(games);
            }
            else {
                var game = this.gameService.getGame(authToken, Integer.parseInt(gameID));
                return serializer.toJson(game);
            }

        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
            } else {
                response.status(500);
            }
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }

    private String createGameRequest(Request request, Response response) {
        try {
            var authToken = request.headers("authorization");
            var inputGameData = serializer.fromJson(request.body(), GameData.class);
            var gameData = this.gameService.createGame(authToken, inputGameData);
            return serializer.toJson(new CreateGameResponse(gameData));
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
            } else if (e.getMessage().equals("Error: bad request")) {
                response.status(400);
            } else {
                response.status(500);
            }
            return "{ \"message\": \"" + e.getMessage() + "\" }";
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
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }

    private String updateGame(Request request, Response response) {
        try {
            var authToken = request.headers("authorization");
            var gameData = serializer.fromJson(request.body(), GameData.class);
            this.gameService.updateGame(authToken, gameData);
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
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }

}
