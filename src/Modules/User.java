package Modules;

public class User {
    private int userId;
    private String username;
    private String password;
    private String userType;
    private String phoneNumber;

    public User(int userId, String username, String password, String userType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUserType() {
        return this.userType;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String returnStringFormat() {
        return Integer.toString(userId) + "," + username + "," + password + "," + userType + phoneNumber;
    }
}
