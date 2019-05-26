package Modules;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class SuperUserBase extends UserBase {

    public SuperUserBase() throws IOException {
        super();
    }

    @Override
    public boolean createUser(String username, String password) throws IOException {
        return ifUserExistsThenCreate(username, password, "Super");

    }

    public boolean deleteUser(String username) throws IOException {
        return deleteThisUser(username);
    }

    public boolean deleteThisUser(String username) throws IOException {
        return ifUserExistsThenDelete(username);
    }

    public boolean ifUserExistsThenDelete(String username) throws IOException {
        Singleton s = Singleton.getInstance();
        if (s.userList.isEmpty()) {
            return false;
        }
        boolean flag = false;
        int tempIndex = -1;
        for (int i = 0; i < s.userList.size(); i++) {
            if (s.userList.get(i).getUsername().equals(username)) {
                flag = true;
                tempIndex = i;
            }
        }

        if (flag) {
            s.userList.get(tempIndex).notifyObservers();
            s.userList.remove(s.userList.get(tempIndex));
            s.userListInStrings.remove(s.userListInStrings.get(tempIndex));
            accountfile.writeFullListToFile(userFileName, s.userListInStrings);
        }
        return flag;
    }

    public boolean containsUserAndPass(String username, String password) throws IOException {
        if (Singleton.getInstance().userListInStrings.isEmpty()) {
            return false;
        }
        for (String s : Singleton.getInstance().userListInStrings) {
            String[] userArray = s.split(",");
            if (userArray[1].equals(username) && userArray[2].equals(password)) {
                return true;
            }
        }
        return false;
    }
}
