package objects;

import ORM.Attribute;
import ORM.Entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@Entity(name="projects")
public class Project {
    @Attribute(name="name", type="VARCHAR(32)")
    private String name;
    @Attribute(name="amount", type="INTEGER")
    private int amount;
    @Attribute(name="create_time", type="VARCHAR(32)")
    OffsetDateTime date;

    public ArrayList<Aim> aimsList = new ArrayList<Aim>();

    public Project(String name, int amount, OffsetDateTime date) {
        this.name = name;
        this.amount = amount;
        this.date = date;
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
    public OffsetDateTime getDate() {
        return date;
    }
    public void setDate(OffsetDateTime date) {
        this.date = date;
    }
}
