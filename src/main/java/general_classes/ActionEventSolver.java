package general_classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.MainController;
import javafx.collections.FXCollections;
import objects.Aim;
import objects.Command;
import objects.Project;
import server_interaction.MessageSolver;
import server_interaction.PacketOfData;
import server_interaction.Threads.WriteThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static general_classes.Main.*;
import static general_classes.Main.locker;

public class ActionEventSolver {
    private Gson gson;
    private DataInputStream dis;
    private DataOutputStream dos;
    //private SocketChannel socketChannel;
    public ActionEventSolver(DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        gson = new GsonBuilder().create();
        /*try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 9999));
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageCreator = new MessageCreator();
    }

    public void getFirstData() {
        System.out.println("Sending start-Data...");
        write(gson.toJson(messageCreator.firstRead()));
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
    public void getFirstData() {
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

                    /*ByteBuffer buffer = ByteBuffer.wrap(gson.toJson(p).getBytes(StandardCharsets.UTF_8));
                    socketChannel.write(buffer);
                    System.out.println("Данные о создании проекта отправлены");*/

                    if(dis.readUTF().equals("accept")) {
                        dataHolder.getProjects().add(new Project(nameOfProject, 0));
                        MainController.projectsHolder.create(new Project(nameOfProject, 0));
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
                    PacketOfData p = new PacketOfData();
                    p.setCommandType(Command.ADD_AIM);
                    p.setName(nameOfProject+";"+text);
                    p.setPriority(prior);
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
                        int ind = -1;
                        for (int i = 0; i < dataHolder.getProjects().size(); i++) {
                            if(dataHolder.getProjects().get(i).getName().equals(nameOfProject)) {
                                ind = i;
                                dataHolder.getProjects().get(i).getAimsList().add(new Aim(text, prior));
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
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
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
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
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
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
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
                    dos.writeUTF(gson.toJson(p));
                    dos.flush();
                    if(dis.readUTF().equals("accept")) {
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


    //Simple Methods to talk with server
    synchronized public void write(String sms)throws IOException {
        System.out.println("write.start: " + sms);
        dos.writeUTF(sms);
        dos.flush();
        System.out.println("write.end");
    }
    synchronized public String read() throws IOException {
        String line = dis.readUTF();
        System.out.println("read: " + line);
        return line;
    }

    /*public void read() throws IOException {
        String line = null;
        line = dis.readUTF();
        System.out.println(line);
        PacketOfData packetOfData = new Gson().fromJson(line, PacketOfData.class);
        dataHolder.setProjects(packetOfData.getProjectsList());
        //dataHolder.showAllProjects();
        mainController.putDataToObservableList();
    }
    public void write(String readyPacket){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Writing to server - " + readyPacket);
                    ByteBuffer buffer = ByteBuffer.wrap(readyPacket.getBytes(StandardCharsets.UTF_8));
                    socketChannel.write(buffer);
                    *//*dos.writeUTF(sms);
                    dos.flush();*//*
                } catch (IOException e) { System.out.println("Caused problem in writing to server"); }
            }
        }).start();
    }*/
}
