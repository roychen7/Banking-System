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
        List<String> userList = accountfile.readFromFile(loginFileName);
        int index = 0;
         for (String s : userList) {
            String[] myUser = s.split(",");
            if ((username.equals(myUser[1]) && password.equals(myUser[2]))) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public int findUserId(String username, String password) throws IOException {
        List<String> userList = accountfile.readFromFile(loginFileName);
        return Integer.parseInt(userList.get(findUserRow(username, password)).split(",")[0]);
    }

    public User findUser(int userId) throws IOException {
        List<String> userList = accountfile.readFromFile(loginFileName);
        for (String s : userList) {
            String[] userArray = s.split(",");
            if (userId == Integer.parseInt(userArray[0])) {
                User user = new User(userId, userArray[1], userArray[2], userArray[3]);
                return user;
            }
        } return null;
    }

    public boolean checkUserExists(String username, String password) throws IOException {
        return findUserRow(username, password) > -1;
    }

    public void updatePhoneNumber(String phoneNumber, int userId) throws IOException {
        User user = findUser(userId);
        if (user!=null) {
            user.setPhoneNumber(phoneNumber);
        }
    }

}