package Modules;


import java.io.IOException;

public interface Subject {

    public void registerObs(Observer obs);
    public void unregisterObs(Observer obs);
    public void notifyObservers() throws IOException;
}
