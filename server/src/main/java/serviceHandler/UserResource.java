package serviceHandler;

import com.google.gson.Gson;
import dataAccess.MemoryGameDAO;
import model.UserData;
import service.ClearService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;

public class UserResource {
    final UserService userService;
    final Gson serializer;
    public UserResource(UserService userService, Gson serializer){
        this.userService = userService;
        this.serializer = serializer;
    }
    public void registerRoutes(){
        Spark.post("/user", this::registerRequest);
    }
    private String registerRequest(Request request, Response response){
        var userData = serializer.fromJson(request.body(), UserData.class);
        var authData = this.userService.register(userData);
        return serializer.toJson(authData);
    }
}
