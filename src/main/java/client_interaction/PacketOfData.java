package client_interaction;

import objects.Aim;
import objects.Project;
import java.io.Serializable;
import java.util.ArrayList;

public class PacketOfData implements Serializable {
    //TODO: create ENUM of commands
    int connectionId;
    public final byte FIRST_READ = 0;
    public final byte ADD = 1;
    public final byte CHANGE = 2;
    public final byte BLOCK_EDITING = 3;
    private byte commandType;
    public String project;
    public String aim;
    int priority;
    public ArrayList<Project> projectsList = new ArrayList<Project>();
    private ArrayList<Integer> blockedData;

    /*Getters, setters*/
    public byte getCommandType() {return commandType;}
    public void setCommandType(byte commandType) {this.commandType = commandType;}
    public ArrayList<Project> getProjectsList() {
        return projectsList;
    }
}
