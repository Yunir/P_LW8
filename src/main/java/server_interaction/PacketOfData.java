package server_interaction;

import objects.Project;
import java.io.Serializable;
import java.util.ArrayList;

public class PacketOfData implements Serializable {
    //TODO: create ENUM of commands
    private final byte FIRST_READ = 0;
    private final byte ADD = 1;
    private final byte CHANGE = 2;
    private final byte BLOCK_EDITING = 3;
    private byte commandType;
    public ArrayList<Project> projectsList = new ArrayList<Project>();
    private ArrayList<Integer> blockedData;

    /*Getters, setters*/
    public byte getCommandType() {return commandType;}
    public void setCommandType(byte commandType) {this.commandType = commandType;}
    public ArrayList<Project> getProjectsList() {
        return projectsList;
    }
    public byte get_FIRST_READ() {
        return FIRST_READ;
    }
    public byte get_ADD() {
        return ADD;
    }
    public byte get_CHANGE() {
        return CHANGE;
    }
    public byte get_BLOCK_EDITING() {
        return BLOCK_EDITING;
    }
}
