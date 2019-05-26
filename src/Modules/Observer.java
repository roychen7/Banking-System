package Modules;

import java.io.IOException;

public interface Observer {
    public void update(Subject sub) throws IOException;
    public void setSubject(Subject sub);
}
