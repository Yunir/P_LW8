package server_interaction;


import objects.Project;

/**
 * Created by Yunicoed on 02.06.2017.
 */
public class MessageCreator {
    PacketOfData packetOfData;
    public PacketOfData firstRead() {
        packetOfData = new PacketOfData();
        packetOfData.setCommandType(packetOfData.FIRST_READ);
        return packetOfData;
    }

    public PacketOfData addProject(String nameOfProject) {
        packetOfData = new PacketOfData();
        packetOfData.setCommandType(packetOfData.ADD);
        packetOfData.project = nameOfProject;
        return packetOfData;
    }
}
