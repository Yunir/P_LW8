package objects;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class Project {
    private String name;
    private int amount;
    OffsetDateTime d;
    public ArrayList<Aim> aimsList = new ArrayList<Aim>();

    public Project(String name, int amount, OffsetDateTime dd) {
        this.name = name;
        this.amount = amount;
        d = dd;
    }

    /*Getters, setters*/
    public ArrayList<Aim> getAimsList() {return aimsList;}
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
