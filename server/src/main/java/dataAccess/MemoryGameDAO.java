package dataAccess;

public class MemoryGameDAO implements GameDAO {

    @Override
    public void clear() {
       System.out.print("hello from memory game dao");
    }

}
