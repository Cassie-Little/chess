import com.google.gson.Gson;
import dataAccess.MemoryUserDAO;
import org.eclipse.jetty.server.Authentication;
import service.SessionService;
import service.UserService;
import serviceHandler.ClearResource;
import serviceHandler.SessionResource;
import serviceHandler.UserResource;
import spark.*;

public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        var userDAO = new MemoryUserDAO();
        var gson = new Gson();
        new ClearResource().registerRoutes();
        new UserResource(new UserService(userDAO), gson).registerRoutes();
        new SessionResource(new SessionService(userDAO), gson).loginRoutes();
        new SessionResource(new SessionService(userDAO), gson).logoutRoutes();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
