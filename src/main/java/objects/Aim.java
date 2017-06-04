package objects;

import java.io.Serializable;

/**
 * Created by Yunicoed on 16.05.2017.
 */
public class Aim {
    public String name;
    public int priority;

    public Aim(String name, int priority) {
        this.name = name;
        this.priority = priority;
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