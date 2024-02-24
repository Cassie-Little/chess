package service;

import dataAccess.GameDAO;

public class ClearService {

    private final GameDAO gameDAO;

    public ClearService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
    public void clear(){
        this.gameDAO.clear();
    }
}
