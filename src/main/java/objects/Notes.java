package objects;

public class Notes implements Comparable{
    public Notes(String text, int importance) {
        this.text = text;
        this.importance = importance;
    }

    private String text;
    private int importance;

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

    @Override
    public int compareTo(Object o) {
        return this.getText().compareTo(((Notes)o).getText());
    }
}
