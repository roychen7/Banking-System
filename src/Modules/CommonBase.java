package Modules;

import java.util.List;

public class CommonBase {

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