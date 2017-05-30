package objects;

import java.io.Serializable;

/**
 * Created by Yunicoed on 16.05.2017.
 */
public class Project {
    public int id;
    public String name;
    public int amount;

    public Project(int id, String s, int amount) {
        this.id = id;
        name = s;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
