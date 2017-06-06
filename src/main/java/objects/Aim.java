package objects;

import java.time.OffsetDateTime;

/**
 * Created by Yunicoed on 16.05.2017.
 */
public class Aim {
    public String name;
    public int priority;
    OffsetDateTime d;
    public Aim(String name, int priority, OffsetDateTime dd) {
        this.name = name;
        this.priority = priority;
        d = dd;
    }

    /*Getters, setters*/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

}