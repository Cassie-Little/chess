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

public class Server {


    public int run(int desiredPort) {
        try {
            Spark.port(desiredPort);

            Spark.staticFiles.location("web");

            // Register your endpoints and handle exceptions here.
            //var userDAO = new MemoryUserDAO();
            //var gameDAO = new MemoryGameDAO();
            //var authDAO = new MemoryAuthDAO();
            var userDAO = new SQLUserDAO();
            var gameDAO = new SQLGameDAO();
            var authDAO = new SQLAuthDAO();
            var gson = new Gson();
            new UserResource(new UserService(userDAO, authDAO), gson).registerRoutes();
            new SessionResource(new SessionService(userDAO, authDAO), gson).registerRoutes();
            new GameResource(new GameService(gameDAO, userDAO, authDAO), gson).registerRoutes();
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
