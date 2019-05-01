package Modules;

import java.io.IOException;

public class RegularUserBase extends UserBase {

    private AccountFile accountfile;
    private LoginClass loginclass;

    public RegularUserBase() throws IOException {
        accountfile = new AccountFile();
    }

    @Override
    public boolean createUser(String username, String password) throws IOException {
        return ifUserExistsThenCreate(username, password, "Regular");
    }
}