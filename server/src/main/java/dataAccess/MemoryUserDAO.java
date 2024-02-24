package dataAccess;

import model.UserData;
import model.AuthData;

import java.util.UUID;

public class MemoryUserDAO implements UserDAO{
    @Override
    public AuthData register(UserData userData) {
        System.out.printf("username: %s, password: %s, email: %s",
                userData.username(), userData.password(), userData.email());
        return new AuthData(UUID.randomUUID().toString(), userData.username());
    }
    public AuthData login(UserData userData) {
        System.out.print("logged in");
        return new AuthData(UUID.randomUUID().toString(), userData.username());
    }
    public AuthData logout() {
        System.out.print("logged out");
        return null;
    }
}
