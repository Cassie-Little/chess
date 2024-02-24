package dataAccess;

import model.UserData;
import model.AuthData;

public interface UserDAO {
    AuthData register(UserData userData);
    AuthData login(UserData userData);
    AuthData logout();
}
