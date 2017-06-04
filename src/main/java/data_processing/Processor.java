package data_processing;

import client_interaction.PacketOfData;
import objects.Command;
import objects.Project;

import static main.Main.*;

/**
 * Created by Yunicoed on 01.06.2017.
 */
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
    public String analyzeMessage(String message) {
        PacketOfData packetOfData = messageSolver.deserializePacketOfData(message);
        if(packetOfData.getCommandType() == Command.FIRST_READ) {
            packetOfData.projectsList = dataHolder.getProjectsList();
        } else if (packetOfData.getCommandType() == Command.ADD_PROJECT) {
            //script
            dataHolder.getProjectsList().add(new Project(packetOfData.getName(), 0));
            messageSolver.addProject(packetOfData.getName());
            packetOfData.projectsList = dataHolder.getProjectsList();

            synchronized (generalPacketOfData) {
                System.out.println("Data changed");
                notifyEveryone = true;
                temp = packetOfData;
                generalPacketOfData.notifyAll();
                System.out.println("Notified everyone, that we have new Data");
            }
        }
        //если message - прочитать - отправить message
        //TODO finish work with refresh data
        return messageSolver.serializePacketOfData(packetOfData);
    }


}
