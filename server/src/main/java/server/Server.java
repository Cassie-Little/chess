package server;

import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import service.GameService;
import service.SessionService;
import service.UserService;
import serviceHandler.GameResource;
import serviceHandler.SessionResource;
import serviceHandler.UserResource;
import spark.Spark;
import websocket.WebSocketHandler;

public class Server {

    public int run(int desiredPort) {
        try {
            Spark.port(desiredPort);
            var gson = new Gson();
            var userDAO = new SQLUserDAO();
            var gameDAO = new SQLGameDAO();
            var authDAO = new SQLAuthDAO();
            var gameService = new GameService(gameDAO, userDAO, authDAO);
            var webSocketHandler = new WebSocketHandler(gson, gameService);
            Spark.staticFiles.location("web");
            Spark.webSocket("/connect", webSocketHandler);


            // Register your endpoints and handle exceptions here.

            new UserResource(new UserService(userDAO, authDAO), gson).registerRoutes();
            new SessionResource(new SessionService(userDAO, authDAO), gson).registerRoutes();
            new GameResource(gameService, gson).registerRoutes();
            Spark.awaitInitialization();
            return Spark.port();
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
        return desiredPort;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
