package ORM;

import java.util.LinkedHashMap;
import java.util.Map;

public class TableData {
    private String name;
    private Map<String, String> attributes = new LinkedHashMap<>();

    public TableData(String name) {
        this.name = name;
    }

    public String getSQLAttributes  () {
        StringBuilder builder = new StringBuilder(" (");
        builder.append(attributes.keySet().toArray()[0]);
        for (int i = 1; i < attributes.size(); i++) {
            builder.append(", " + attributes.keySet().toArray()[i]);
        }
        builder.append(")");
        return builder.toString();
    }

    /*Getters, setters*/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<String, String> getAttributes() {
        return attributes;
    }
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
