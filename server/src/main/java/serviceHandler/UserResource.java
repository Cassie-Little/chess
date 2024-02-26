package serviceHandler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.UserData;
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
        try {
            var userData = serializer.fromJson(request.body(), UserData.class);
            var authData = this.userService.register(userData);
            return serializer.toJson(authData);
        }
        catch (DataAccessException e){
            if (e.getMessage().equals("Error: bad request")) {
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
