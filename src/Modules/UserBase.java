package Modules;

import java.io.IOException;

public abstract class UserBase {

    private ClassToGetNewId NewIdCalculatorObj;
    protected String userFileName;
    protected AccountFile accountfile;

    public UserBase() {
        accountfile = new AccountFile();
        userFileName = System.getProperty("user.dir") + "/src/data/Login";
        NewIdCalculatorObj = new ClassToGetNewId();
    }

    abstract public boolean createUser(String username, String password) throws IOException;


    // returns true if user exists, false if user does not exist
    public boolean checkUserExists(String username, String password) throws IOException {
        if (Singleton.getInstance().userListInStrings != null) {
            for (String s : Singleton.getInstance().userListInStrings) {
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

    // finds the user with username and password, returns null otherwise
    public User findUser(String username, String password) {
        for (User u : Singleton.getInstance().userList) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // creates user from given string array, assumes that userArray is passed in as a correct string array
    // to create a user
    public User createUserFromStringArray(String[] userArray) {
        return new User(Integer.parseInt(userArray[0]), userArray[1], userArray[2], userArray[3]);
    }

    // creates new user, updating singleton's lists and writing to file 
    public void makeNewUser(String username, String password, String usertype) throws IOException {
        Singleton s = Singleton.getInstance();
        Integer newUserId = NewIdCalculatorObj.getNewId(s.userListInStrings);
        String User = newUserId + "," + username + "," + password + "," + usertype + ",";
        accountfile.writeToFile(userFileName, User);
        s.userListInStrings.add(User);
        User newUser = new User(newUserId, username, password, usertype);
        s.userList.add(newUser);
    }

    // returns true if user creation is successful, false if user already exists
    public boolean ifUserDoesntExistsThenCreate(String user, String pass, String type) throws IOException {
        if (checkUserExists(user, pass)) {
            return false;
        } else {
            makeNewUser(user, pass, type);
            return true;
        }
    }

    // initiates the singleton to create its list of users from the user file
    public void initializeSingleton() throws IOException {
        Singleton sing = Singleton.getInstance();
        sing.userListInStrings = accountfile.readFromFile(userFileName);
        for (String s : sing.userListInStrings) {
            String[] arr = s.split(",");
            sing.userList.add(createUserFromStringArray(arr));
        }
        if (!sing.userList.isEmpty()) {
            initUsers();
        }
    }

    // initiaites users in the singleton's list of users to have their list of observers
    public void initUsers() {
        /*
        initUserOffsets();
        */
        for (User u : Singleton.getInstance().userList) {
            for (int i = 0; i < SingletonAccount.getInstance().accountModelList.size(); i++) {
                AccountModel acc = SingletonAccount.getInstance().accountModelList.get(i);
                if (acc.getUserId() == u.getUserId()) {
                    u.registerObs(acc);
                }
            }
        }
    }

    public String userToString(User u) {
        return Integer.toString(u.getUserId()) + "," + u.getUsername() + "," + u.getPassword() + "," +
                u.getType() + ",";
    }

    //code to calculate byte offset in text files used for RandomAccessFile rw operations, eventually decided
    //to not use this method
    /*
    public void initUserOffsets() {
        Singleton s = Singleton.getInstance();
        s.userList.get(0).setOffSet(0);
        if (s.userList.size()==1) {
            return;
        }
        for (int i = 1; i < s.userListInStrings.size(); i++) {
            int j = 1 + s.userList.get(i-1).getOffSet()+s.userListInStrings.get(i-1).length();
            s.userList.get(i).setOffSet(j);
        }

    }
    */
}
