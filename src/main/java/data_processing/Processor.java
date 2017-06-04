package data_processing;

import client_interaction.PacketOfData;
import objects.Command;
import objects.Project;

import static general_classes.Main.*;

public class Processor {
    Database DB;
    private MessageSolver messageSolver;

    public Processor() {
        DB = new Database();
        messageSolver = new MessageSolver(DB.getDbConnection());
    }

    public boolean checkLogPass (String msg) {
        return messageSolver.checkLogPass(msg);
    }

    //TODO transfer to MessageCreator
    public String analyzeMessage(String message, int idOfConnection) {
        PacketOfData packetOfData = messageSolver.deserializePacketOfData(message);
        if(packetOfData.getCommandType() == Command.FIRST_READ) {
            packetOfData.projectsList = dataHolder.getProjectsList();
        } else if (packetOfData.getCommandType() == Command.ADD_PROJECT) {
            //script
            dataHolder.getProjectsList().add(new Project(packetOfData.getName(), 0));
            messageSolver.addProject(packetOfData.getName());
            packetOfData.projectsList = dataHolder.getProjectsList();

            locker.lock();
            System.out.println("Data changed");
            notifyEveryone = true;
            generalPacketOfData = packetOfData;
            generalPacketOfData.setConnectionId(idOfConnection);
            updates.signalAll();
            System.out.println("Notified everyone, that we have new Data");
            locker.unlock();
        }
        return messageSolver.serializePacketOfData(packetOfData);
    }


}
