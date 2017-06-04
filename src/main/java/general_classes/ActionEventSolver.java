package general_classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.MainController;
import objects.Command;
import objects.Project;
import server_interaction.MessageCreator;
import server_interaction.MessageSolver;
import server_interaction.PacketOfData;
import server_interaction.Threads.WriteThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static general_classes.Main.data;
import static general_classes.Main.mainController;
import static general_classes.Main.toServer;

public class ActionEventSolver {
    private Gson gson;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ActionEventSolver(DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        gson = new GsonBuilder().create();
    }

    public void addProject(String nameOfProject) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PacketOfData p = new PacketOfData();
                    p.setCommandType(Command.ADD_PROJECT);
                    p.setName(nameOfProject);
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
                        data.getProjects().add(new Project(nameOfProject, 0));
                        MainController.projectsHolder.create(new Project(nameOfProject, 0));
                    } else {
                        System.out.println("Denied!");
                    }
                    //data.showAllProjects();
                    mainController.putDataToObservableList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
