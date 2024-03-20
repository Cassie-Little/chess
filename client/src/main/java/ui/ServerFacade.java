package ui;

import chess.ChessGame;
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
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null,  null, null, null);
    }

    public AuthData register(UserData userData) throws ResponseException {
        var path = "/user";
        var authData = this.makeRequest("POST", path, userData , null, AuthData.class, null);
        authToken = authData.authToken();
        return authData;
    }

    public AuthData login(UserData userData) throws ResponseException {
        var path = "/session";
        var authData = this.makeRequest("POST", path, userData, null, AuthData.class, null);
        authToken = authData.authToken();
        return authData;
    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, authToken, null, null);
    }

    public GameListData listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null,  authToken, GameListData.class, null);
    }

    public CreateGameResponse createGame(GameData gameData) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, gameData, authToken, CreateGameResponse.class, null);
    }

    public ChessGame joinGame() throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, null,  authToken, ChessGame.class, ChessBoardUI.class);
    }



    private <T> T makeRequest(String method, String path, Object request, String header,  Class<T> responseClass, Class<T> chessboardUIClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("Authorization", header);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass, chessboardUIClass);
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

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass, Class<T> chessboardUIClass) throws IOException {
        T response = null;
        ChessBoardUI response2 = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
//                if (chessboardUIClass == null) {
//                    response2 = new ChessBoardUI();
//                    return (T) response2;
//                }
            }
        }
        return response;

    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
