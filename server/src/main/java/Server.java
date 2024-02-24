import com.google.gson.Gson;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import service.GameService;
import service.SessionService;
import service.UserService;
import serviceHandler.ClearResource;
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
        var gson = new Gson();
        new ClearResource().registerRoutes();
        new UserResource(new UserService(userDAO), gson).registerRoutes();
        new SessionResource(new SessionService(userDAO), gson).loginRoutes();
        new SessionResource(new SessionService(userDAO), gson).logoutRoutes();
        new GameResource(new GameService(gameDAO), gson).listGamesRoutes();
        new GameResource(new GameService(gameDAO), gson).createGameRoutes();
        new GameResource(new GameService(gameDAO), gson).joinGame();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
