package serviceHandler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import model.UserData;
import service.SessionService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class SessionResource {
    final SessionService sessionService;
    final Gson serializer;

    public SessionResource(SessionService sessionService, Gson serializer) {
        this.sessionService = sessionService;
        this.serializer = serializer;
    }

    public void registerRoutes(){
        Spark.post("/session", this::loginRequest);
        Spark.delete("/session", this::logoutRequest);
    }
    private String loginRequest(Request request, Response response){
        try {
            var userData = serializer.fromJson(request.body(), UserData.class);
            var authData = this.sessionService.login(userData);
            return serializer.toJson(authData);
        }
        catch (DataAccessException e){
            if (e.getMessage().equals("Error: unauthorized")){
                response.status(401);
            }
            else {
                response.status(500);
            }
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }

    private String logoutRequest(Request request, Response response) {
        try {
            var authToken = request.headers("authorization");
            this.sessionService.logout(authToken);
            return "";
        }
        catch (DataAccessException e){
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
            }else {
                response.status(500);
            }
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }
}
