package Modules;

import java.util.ArrayList;
import java.util.List;

public class SingletonAccount {

    private static SingletonAccount instance;
    public List<AccountModel> accountModelList;
    public List<String> accountListInStrings;

    // SingletonAccount constructor initializes its lists to being empty
    private SingletonAccount() {
        accountListInStrings = new ArrayList<>();
        accountModelList = new ArrayList<>();
    }

    public static SingletonAccount getInstance() {
        if (instance == null) {
            instance = new SingletonAccount();
        }
        return instance;
    }
}
