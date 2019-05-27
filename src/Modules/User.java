package Modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Subject {
    private int userId;
    private String username;
    private String password;
    private String userType;
    private List<Observer> observers;
    private int offSet;


    public User(int userId, String username, String password, String userType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
        observers = new ArrayList<>();
    }

    public User(int userId, String username, String password, String userType, List<Observer> observers) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.observers = observers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return userId == that.getUserId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return userType;
    }

    public void setOffSet(int i) {
        this.offSet = i;
    }

    public int getOffSet() {
        return offSet;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    @Override
    public void registerObs(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void unregisterObs(Observer obs) {
        observers.remove(obs);
    }

    @Override
    public void notifyObservers() throws IOException {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(this);
        }
    }
}
