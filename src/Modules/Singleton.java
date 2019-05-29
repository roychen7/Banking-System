package Modules;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton instance;
    public List<User> userList;
    public List<String> userListInStrings;

    // Singleton constructor initializes its lists to being empty
    private Singleton() {
        userList = new ArrayList<>();
        userListInStrings = new ArrayList<>();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
