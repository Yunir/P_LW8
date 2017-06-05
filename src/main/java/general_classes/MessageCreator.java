package general_classes;

import objects.Command;
import objects.Project;
import server_interaction.PacketOfData;

public class MessageCreator {
    public static PacketOfData firstRead() {
        PacketOfData packetOfData = new PacketOfData();
        packetOfData.setCommandType(Command.FIRST_READ);
        return packetOfData;
    }

    public static PacketOfData addProject(String nameOfProject) {
        PacketOfData packetOfData = new PacketOfData();
        packetOfData.setCommandType(Command.ADD_PROJECT);
        packetOfData.setName(nameOfProject);
        return packetOfData;
    }
}
