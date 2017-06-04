package general_classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.MainController;
import javafx.collections.FXCollections;
import objects.Command;
import objects.Project;
import server_interaction.PacketOfData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static general_classes.Main.data;
import static general_classes.Main.mainController;

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateProject(String oldName, String newName) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PacketOfData p = new PacketOfData();
                    p.setCommandType(Command.UPDATE_PROJECT);
                    p.setName(oldName+";"+newName);
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
                        for (int i = 0; i < data.getProjects().size(); i++) {
                            if(data.getProjects().get(i).getName().equals(oldName)) {
                                data.getProjects().get(i).setName(newName);
                                break;
                            }
                        }

                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(data.getProjects()));
                        mainController.getProjectsTable().refresh();
                    } else {
                        System.out.println("Denied!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
