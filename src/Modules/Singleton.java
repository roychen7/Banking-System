package Modules;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton instance;
    public List<User> userList;
    public List<String> userListInStrings;
    AccountFile af;

    private Singleton() {
        userList = new ArrayList<>();
        userListInStrings = new ArrayList<>();
        af = new AccountFile();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
