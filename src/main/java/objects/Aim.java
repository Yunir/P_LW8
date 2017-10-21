package objects;

import ORM.Attribute;
import ORM.Entity;

import java.time.OffsetDateTime;

/**
 * Created by Yunicoed on 16.05.2017.
 */
@Entity(name="aims")
public class Aim {
    @Attribute(name="name", type="VARCHAR(32)")
    private String name;
    @Attribute(name="priority", type="INTEGER")
    private int priority;
    @Attribute(name="create_time", type="VARCHAR(32)")
    private OffsetDateTime date;

    public Aim(String name, int priority, OffsetDateTime date) {
        this.name = name;
        this.priority = priority;
        this.date = date;
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
    public OffsetDateTime getDate() {
        return date;
    }
    public void setDate(OffsetDateTime date) {
        this.date = date;
    }
}
