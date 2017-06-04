package objects;

/**
 * Created by Yunicoed on 16.05.2017.
 */
public class Aim {
    public int id;
    public String name;
    public int priority;

    public Aim(int id, String name, int priority) {
        this.id = id;
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
