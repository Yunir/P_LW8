package general_classes;


import objects.Command;
import objects.Project;
import server_interaction.PacketOfData;

public class MessageCreator {
    PacketOfData packetOfData;
    public PacketOfData firstRead() {
        packetOfData = new PacketOfData();
        packetOfData.setCommandType(Command.FIRST_READ);
        return packetOfData;
    }

    public PacketOfData addProject(String nameOfProject) {
        packetOfData = new PacketOfData();
        packetOfData.setCommandType(Command.ADD_PROJECT);
        packetOfData.setName(nameOfProject);
        return packetOfData;
    }
}
