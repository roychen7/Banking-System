package Modules;


import java.io.IOException;

public class LoginClass {
    private String loginFileName;
    private AccountFile accountfile;
    private UserInfo ui;

    public LoginClass() throws IOException {
        loginFileName = System.getProperty("user.dir") + "/src/data/Login";
        accountfile = new AccountFile();
        ui = new UserInfo();
    }


    public User login (String username, String password) throws IOException {
        if (ui.checkUserExists(username, password)) {
            int i = ui.findUserId(username, password);
            return ui.findUser(i);
        }
        return null;
    }
}
