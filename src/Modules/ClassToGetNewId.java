package Modules;

import java.util.List;

// class's entire purpose is to serve creating the getNewId function
public class ClassToGetNewId {

    // returns new Id, calculated by incrementing from last seen Id in myList
    public int getNewId(List<String> myList) {
        if (myList.size() >= 1) {
            String lastUser = myList.get(myList.size()-1);
            String[] loginInfo = lastUser.split(",");
            return Integer.parseInt(loginInfo[0])+1;
        }
        else {
            return 1;
        }
    }
}