package general_classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.MainController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import objects.Aim;
import objects.Command;
import server_interaction.PacketOfData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;

import static general_classes.Main.*;
import static general_classes.Main.locker;

public class ActionEventSolver {
    private Gson gson;
    private DataInputStream dis;
    private DataOutputStream dos;
    public ActionEventSolver(DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        gson = new GsonBuilder().create();
    }

    public void getFirstFullPacket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                locker.lock();
                System.out.println("lock.ToServer");
                try {
                    if(!MainController.confirmationReceived) accessToResource.await();
                    getFirstData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    locker.unlock();
                }
            }
        }).start();
    }
    private void getFirstData() {
        try {
            write(gson.toJson(MessageCreator.firstRead()));
            String packet = read();
            PacketOfData packetOfData = new Gson().fromJson(packet, PacketOfData.class);
            dataHolder.setProjects(packetOfData.getProjectsList());
            mainController.putDataToObservableList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void awaitOfUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean allIsGood = true;
                while (allIsGood) {
                    try {
                        String packet = read();
                        PacketOfData packetOfData = new Gson().fromJson(packet, PacketOfData.class);
                        dataHolder.setProjects(packetOfData.getProjectsList());
                        mainController.putDataToObservableList();
                        System.out.println("Wow, new Information");
                    } catch (IOException e) {
                        allIsGood = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                fromServer.getConnector().showLostConnection();
                            }
                        });
                    }
                }
            }
        }).start();

    }

    public void addProject(String nameOfProject) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    write(gson.toJson(MessageCreator.addProject(nameOfProject)));
                    if(read().equals("accept")) {
                        String[] splitedLine = nameOfProject.split(";");
                        for (int i = 0; i < splitedLine.length; i++) {
                            dataHolder.addProject(splitedLine[i]);
                            MainController.projectsHolder.create(splitedLine[i]);
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                mainController.getProjectsTable().refresh();
                            }
                        });
                    } else {
                        System.out.println("Denied!");
                    }
                    //dataHolder.showAllProjects();
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
                    write(gson.toJson(MessageCreator.addAim(nameOfProject, text, prior)));
                    if(read().equals("accept")) {
                        for (int i = 0; i < dataHolder.getProjects().size(); i++) {
                            if(dataHolder.getProjects().get(i).getName().equals(nameOfProject)) {
                                dataHolder.getProjects().get(i).getAimsList().add(new Aim(text, prior, OffsetDateTime.now()));
                                dataHolder.getProjects().get(i).setAmount(dataHolder.getProjects().get(i).getAmount()+1);
                                break;
                            }
                        }
                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(dataHolder.getProjects()));

                        mainController.getProjectsTable().getItems().clear();
                        mainController.getProjectsTable().getItems().addAll(mainController.projectsHolder.getProjectsObsList());
                        mainController.getAimsTable().getItems().clear();
                        mainController.disableUpdateDeleteButtons();
                        //MainController.projectsHolder.getProjectsObsList().get(ind).getAimsList().add(new Aim(text, prior));
                    } else {
                        System.out.println("Denied!");
                    }
                    //dataHolder.showAllProjects();
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
                    write(gson.toJson(p));
                    if(read().equals("accept")) {
                        int indP = -1;
                        int indA = -1;
                        for (int i = 0; i < dataHolder.getProjects().size(); i++) {
                            if(dataHolder.getProjects().get(i).getName().equals(projectName)) {
                                indP = i;
                                for (int j = 0; j < dataHolder.getProjects().get(i).getAimsList().size(); j++) {
                                    if(dataHolder.getProjects().get(i).getAimsList().get(j).getName().equals(oldAimName)) {
                                        dataHolder.getProjects().get(i).getAimsList().get(j).setName(text);
                                        dataHolder.getProjects().get(i).getAimsList().get(j).setPriority(prior);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(dataHolder.getProjects()));

                        mainController.getProjectsTable().getItems().clear();
                        mainController.getProjectsTable().getItems().addAll(mainController.projectsHolder.getProjectsObsList());
                        mainController.getAimsTable().getItems().clear();
                        mainController.disableUpdateDeleteButtons();
                        //MainController.projectsHolder.getProjectsObsList().get(ind).getAimsList().add(new Aim(text, prior));
                    } else {
                        System.out.println("Denied!");
                    }
                    //dataHolder.showAllProjects();
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
                    write(gson.toJson(p));
                    if(read().equals("accept")) {
                        for (int i = 0; i < dataHolder.getProjects().size(); i++) {
                            if(dataHolder.getProjects().get(i).getName().equals(oldName)) {
                                dataHolder.getProjects().get(i).setName(newName);
                                break;
                            }
                        }

                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(dataHolder.getProjects()));
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
                    write(gson.toJson(p));
                    if(read().equals("accept")) {
                        int deleteNum = -1;
                        for (int i = 0; i < dataHolder.getProjects().size(); i++) {
                            if(dataHolder.getProjects().get(i).getName().equals(name)) {
                                deleteNum = i;
                                break;
                            }
                        }
                        dataHolder.getProjects().remove(deleteNum);
                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(dataHolder.getProjects()));
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
                    write(gson.toJson(p));
                    if(read().equals("accept")) {
                        int deleteNum = -1;
                        for (int i = 0; i < dataHolder.getProjects().size(); i++) {
                            if(dataHolder.getProjects().get(i).getName().equals(project)) {
                                deleteNum = i;
                                for (int j = 0; j < dataHolder.getProjects().get(i).getAimsList().size(); j++) {
                                    if(dataHolder.getProjects().get(i).getAimsList().get(j).getName().equals(aim)) {
                                        dataHolder.getProjects().get(i).getAimsList().remove(j);
                                        dataHolder.getProjects().get(i).setAmount(dataHolder.getProjects().get(i).getAmount()-1);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        mainController.projectsHolder.setProjectsObsList(FXCollections.observableArrayList(dataHolder.getProjects()));
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
    public void createObj() {
        try {
            write(gson.toJson(MessageCreator.putClass()));
            if(read().equals("accept")) {
                System.out.println("Congratulations, my friend!");
            } else {
                System.out.println("Denied!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Simple Methods to talk with server
    public void write(String sms)throws IOException {
        System.out.println("write.start: " + sms);
        dos.writeUTF(sms);
        dos.flush();
        System.out.println("write.end");
    }
    public String read() throws IOException {
        String line = dis.readUTF();
        System.out.println("read: " + line);
        return line;
    }
}