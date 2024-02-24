package serviceHandler;

import com.google.gson.Gson;
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

    public void loginRoutes(){
        Spark.post("/session", this::loginRequest);
    }
    private String loginRequest(Request request, Response response){
        var userData = serializer.fromJson(request.body(), UserData.class);
        var authData = this.sessionService.login(userData);
        return serializer.toJson(authData);
    }
}
