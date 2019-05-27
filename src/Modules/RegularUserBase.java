package Modules;

import java.io.IOException;

public class RegularUserBase extends UserBase {

    public RegularUserBase() throws IOException {
        super();
    }

    // returns true if "Regular" user was created, false otherwise
    public boolean createUser(String username, String password) throws IOException {
        return ifUserDoesntExistsThenCreate(username, password, "Regular");
    }
}