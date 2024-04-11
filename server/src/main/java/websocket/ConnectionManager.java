package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;


public class ConnectionManager {
    public final ArrayList<Connection> connections = new ArrayList<>();

    public void add(String visitorName, Session session, int gameID) {
        var connection = getConnection(visitorName, gameID);
        if (connection == null) {
            connection = new Connection(visitorName, session, gameID);
            connections.add(connection);
        }
        connection.session = session;
    }

    public void remove(String visitorName, int gameID) {
        Connection connection = getConnection(visitorName, gameID);
        if (connection != null){
            connections.remove(connection);
        }
    }

    private Connection getConnection(String visitorName, int gameID) {
        Connection connection = null;
        for (var c : connections) {
            if (c.visitorName.equals(visitorName) && c.gameID == gameID){
                connection = c;
                break;
            }
        }
        return connection;
    }

    public void broadcast(String excludeVisitorName, int gameID, String serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName) && c.gameID == gameID) {
                    c.send(serverMessage);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c);
        }
    }
}
