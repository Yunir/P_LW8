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

    public static PacketOfData addAim(String nameOfProject, String aim, int prior) {
        PacketOfData p = new PacketOfData();
        p.setCommandType(Command.ADD_AIM);
        p.setName(nameOfProject+";"+aim);
        p.setPriority(prior);
        return p;
    }
}
