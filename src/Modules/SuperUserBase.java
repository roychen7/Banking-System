package Modules;

import java.io.IOException;

public class SuperUserBase extends UserBase {

    public SuperUserBase() throws IOException {
        super();
    }

    // returns true if "Super" user was created, false if user already exists
    @Override
    public boolean createUser(String username, String password) throws IOException {
        return ifUserDoesntExistsThenCreate(username, password, "Super");

    }

    // returns true if user was deleted, false if user was not found. Rewrites the user text file once deleted
    // to account for the deleted user, an O(n) runtime operation
    public boolean deleteUser(String username) throws IOException {
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

    // returns true if singleton's list of users contains the specified username and password as a combination
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
