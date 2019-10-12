package general_classes;

import controllers.MainController;
import objects.Aim;
import objects.Command;
import objects.Notes;
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

    public static Object putClass() {
        PacketOfData p = new PacketOfData();
        p.setCommandType(Command.CREATE_OBJECT_TABLE);
        if(MainController.c == Aim.class) {
            p.setPriority(2);
        } else p.setPriority(1);
        return p;
    }

    public static PacketOfData addNote(String text, int i) {
        PacketOfData p = new PacketOfData();
        p.setCommandType(Command.CREATE_NOTE);
        p.setName(text);
        p.setPriority(i);
        return p;
    }
}
