package Modules;

import java.io.IOException;
import java.util.List;

public class UserInfo {
    private AccountFile accountfile = new AccountFile();
    private String loginFileName;

    public UserInfo() {
        loginFileName = System.getProperty("user.dir") + "/src/data/Login";
    }

    public int findUserRow(String username, String password) throws IOException {
        int index = 0;
        for (String s : Singleton.getInstance().userListInStrings) {
            String[] myUser = s.split(",");
            if ((username.equals(myUser[1]) && password.equals(myUser[2]))) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public int findUserId(String username, String password) throws IOException {
        if (checkUserExists(username, password)) {
            return Integer.parseInt(Singleton.getInstance().userListInStrings.get(findUserRow(username, password)).split(",")[0]);
        } else return -1;
    }

    public User findUser(int userId) throws IOException {
        for (User u: Singleton.getInstance().userList) {
            if (u.getUserId()==userId) {
                return u;
            }
        } return null;
    }

    public boolean checkUserExists(String username, String password) throws IOException {
        return findUserRow(username, password) > -1;
    }
}