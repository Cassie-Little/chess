import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import service.GameService;
import service.SessionService;
import service.UserService;
import serviceHandler.GameResource;
import serviceHandler.SessionResource;
import serviceHandler.UserResource;
import spark.*;

public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        var userDAO = new MemoryUserDAO();
        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();
        var gson = new Gson();
        new UserResource(new UserService(userDAO, authDAO), gson).registerRoutes();
        new SessionResource(new SessionService(userDAO), gson).registerRoutes();
        new GameResource(new GameService(gameDAO), gson).registerRoutes();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
