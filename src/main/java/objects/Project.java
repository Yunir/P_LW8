package objects;

import java.util.ArrayList;

public class Project {
    private String name;
    private int amount;
    public ArrayList<Aim> aimsList = new ArrayList<Aim>();

    public Project(String name, int amount) {
        this.name = name;
        this.amount = amount;
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
