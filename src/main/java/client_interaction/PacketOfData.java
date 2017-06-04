package client_interaction;

import objects.Command;
import objects.Project;
import java.io.Serializable;
import java.util.ArrayList;

public class PacketOfData implements Serializable {
    private int connectionId;
    private String name;
    private int priority;
    private Command commandType;
    public ArrayList<Project> projectsList = new ArrayList<Project>();
    private ArrayList<Integer> blockedData;

    /*Getters, setters*/
    public Command getCommandType() {return commandType;}
    public void setCommandType(Command commandType) {this.commandType = commandType;}
    public ArrayList<Project> getProjectsList() {
        return projectsList;
    }
    public void setProjectsList(ArrayList<Project> projectsList) {
        this.projectsList = projectsList;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getConnectionId() {
        return connectionId;
    }
    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }
}
