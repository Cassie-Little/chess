package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;


    public ServerFacade( String url) {
        serverUrl = url;

    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null,  null, null);
    }

    public AuthData register(UserData userData) throws ResponseException {
        var path = "/user";
        var authData = this.makeRequest("POST", path, userData , null, AuthData.class);
        return authData;
    }

    public AuthData login(UserData userData) throws ResponseException {
        var path = "/session";
        var authData = this.makeRequest("POST", path, userData, null, AuthData.class);
        return authData;
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, authToken, null);
    }

    public GameListData listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null,  authToken, GameListData.class);
    }

    public GameData createGame(String authToken, GameData gameData) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, gameData, authToken, GameData.class);
    }

    public void joinGame(String authToken, JoinGameData gameData) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, gameData,  authToken, GameData.class);

    }
    public GameData getGame(String authToken, int gameID) throws ResponseException {
        var path = "/game?gameID="+gameID;
        return this.makeRequest("GET", path, null,  authToken, GameData.class);
    }

    public void updateGame(String authToken, GameData gameData) throws ResponseException {
        var path = "/game";
        this.makeRequest("PATCH", path, gameData, authToken, GameData.class);
    }



    private <T> T makeRequest(String method, String path, Object request, String header,  Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("Authorization", header);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        ChessBoardUI response2 = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;

    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
