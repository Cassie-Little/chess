package serviceHandler;
import dataAccess.MemoryGameDAO;
import service.ClearService;
import spark.*;

public class ClearResource {
    final ClearService clearService;
    public ClearResource(){
        this.clearService = new ClearService(new MemoryGameDAO());
    }
    public void registerRoutes(){
        Spark.delete("/db", this::clearRequest);
    }
    private String clearRequest(Request request, Response response){
        this.clearService.clear();
        return "";
    }
}
