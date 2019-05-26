package Modules;

import java.io.IOException;

public class RegularUserBase extends UserBase {

    public RegularUserBase() throws IOException {
        super();
    }

    public boolean createUser(String username, String password) throws IOException {
        return ifUserExistsThenCreate(username, password, "Regular");
    }
}