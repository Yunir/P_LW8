package objects;


import ORM.Attribute;
import ORM.Entity;

@Entity(name="notes")
public class Notes {
    @Attribute(name="text", type="VARCHAR(32)")
    private String text;
    @Attribute(name="importance", type="INTEGER")
    private int importance;

    public Notes(String text, int importance) {
        this.text = text;
        this.importance = importance;
    }

    /*Getters, setters*/
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getImportance() {
        return importance;
    }
    public void setImportance(int importance) {
        this.importance = importance;
    }
}
