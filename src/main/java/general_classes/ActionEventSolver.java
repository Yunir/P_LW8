package general_classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.MainController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import objects.Aim;
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

    public void addAim(String nameOfProject, String text,int prior) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PacketOfData p = new PacketOfData();
                    p.setCommandType(Command.ADD_AIM);
                    p.setName(nameOfProject+";"+text);
                    p.setPriority(prior);
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
                        int ind = -1;
                        for (int i = 0; i < data.getProjects().size(); i++) {
                            if(data.getProjects().get(i).getName().equals(nameOfProject)) {
                                ind = i;
                                data.getProjects().get(i).getAimsList().add(new Aim(text, prior));
                                data.getProjects().get(i).setAmount(data.getProjects().get(i).getAmount()+1);
                                break;
                            }
                        }
                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(data.getProjects()));

                        mainController.getProjectsTable().getItems().clear();
                        mainController.getProjectsTable().getItems().addAll(mainController.projectsHolder.getProjectsObsList());
                        mainController.getAimsTable().getItems().clear();
                        mainController.disableUpdateDeleteButtons();
                        //MainController.projectsHolder.getProjectsObsList().get(ind).getAimsList().add(new Aim(text, prior));
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

    public void updateAim(String projectName, String oldAimName, String text, int prior) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PacketOfData p = new PacketOfData();
                    p.setCommandType(Command.UPDATE_AIM);
                    p.setName(projectName+";"+oldAimName+";"+text);
                    p.setPriority(prior);
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
                        int indP = -1;
                        int indA = -1;
                        for (int i = 0; i < data.getProjects().size(); i++) {
                            if(data.getProjects().get(i).getName().equals(projectName)) {
                                indP = i;
                                for (int j = 0; j < data.getProjects().get(i).getAimsList().size(); j++) {
                                    if(data.getProjects().get(i).getAimsList().get(j).getName().equals(oldAimName)) {
                                        data.getProjects().get(i).getAimsList().get(j).setName(text);
                                        data.getProjects().get(i).getAimsList().get(j).setPriority(prior);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(data.getProjects()));

                        mainController.getProjectsTable().getItems().clear();
                        mainController.getProjectsTable().getItems().addAll(mainController.projectsHolder.getProjectsObsList());
                        mainController.getAimsTable().getItems().clear();
                        mainController.disableUpdateDeleteButtons();
                        //MainController.projectsHolder.getProjectsObsList().get(ind).getAimsList().add(new Aim(text, prior));
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
                        //TODO: remain active selection of project
                        mainController.getProjectsTable().getItems().clear();
                        mainController.getProjectsTable().getItems().addAll(mainController.projectsHolder.getProjectsObsList());
                        mainController.disableUpdateDeleteButtons();
                    } else {
                        System.out.println("Denied!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void deleteProject(String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PacketOfData p = new PacketOfData();
                    p.setCommandType(Command.DELETE_PROJECT);
                    p.setName(name);
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
                        int deleteNum = -1;
                        for (int i = 0; i < data.getProjects().size(); i++) {
                            if(data.getProjects().get(i).getName().equals(name)) {
                                deleteNum = i;
                                break;
                            }
                        }
                        data.getProjects().remove(deleteNum);
                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(data.getProjects()));
                        //mainController.getProjectsTable().refresh();
                        mainController.getProjectsTable().getItems().clear();
                        mainController.getProjectsTable().getItems().addAll(mainController.projectsHolder.getProjectsObsList());
                        mainController.getAimsTable().getItems().clear();
                        mainController.disableUpdateDeleteButtons();
                    } else {
                        System.out.println("Denied!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void deleteAim(String project, String aim) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PacketOfData p = new PacketOfData();
                    p.setCommandType(Command.DELETE_AIM);
                    p.setName(project+";"+aim);
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
                        int deleteNum = -1;
                        for (int i = 0; i < data.getProjects().size(); i++) {
                            if(data.getProjects().get(i).getName().equals(project)) {
                                deleteNum = i;
                                for (int j = 0; j < data.getProjects().get(i).getAimsList().size(); j++) {
                                    if(data.getProjects().get(i).getAimsList().get(j).getName().equals(aim)) {
                                        data.getProjects().get(i).getAimsList().remove(j);
                                        data.getProjects().get(i).setAmount(data.getProjects().get(i).getAmount()-1);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(data.getProjects()));
                        //mainController.getProjectsTable().refresh();
                        mainController.getProjectsTable().getItems().clear();
                        mainController.getProjectsTable().getItems().addAll(mainController.projectsHolder.getProjectsObsList());
                        mainController.getAimsTable().getItems().clear();
                        mainController.disableUpdateDeleteButtons();
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
