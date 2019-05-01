package Modules;

import java.io.IOException;
import java.util.List;

public abstract class UserBase {

    private CommonBase commonbase;
    private String loginFileName;
    private AccountFile accountfile;
    private List<String> userList;
    private String usertype;
    private UserInfo ui;

    public UserBase() throws IOException {
        accountfile = new AccountFile();
        loginFileName = System.getProperty("user.dir") + "/src/data/Login";
        userList = accountfile.readFromFile(loginFileName);
        usertype = "Regular";
        commonbase = new CommonBase();
    }

    abstract public boolean createUser(String username, String password) throws IOException;


    public boolean checkUserExists(String username, String password) throws IOException {
        if (userList != null) {
            for (String s : userList) {
                String[] myUser = s.split(",");
                if (myUser.length >= 3) {
                    if ((username.equals(myUser[1]) && password.equals(myUser[2]))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void makeNewUser(String username, String password, String usertype) throws IOException {
        Integer newUserId = commonbase.getNewId(userList);
        String User = newUserId + "," + username + "," + password + "," + usertype + "," + " ";
        accountfile.writeToFile(loginFileName, User);
        userList.add(User);
    }

    public User findUserById(int userId) {
        User user;
        for (String s : userList) {
            String[] myUser = s.split(",");
            if (myUser.length >= 0) {
                if (Integer.parseInt(myUser[0]) == userId) {
                    user = new User(Integer.parseInt(myUser[0]), myUser[1], myUser[2],
                            myUser[3]);
                    return user;
                }
            }
        }
        return null;
    }

    public boolean ifUserExistsThenCreate(String user, String pass, String type) throws IOException {
        if (checkUserExists(user, pass)) {
            return false;
        } else {
            makeNewUser(user, pass, type);
            return true;
        }
    }
}
