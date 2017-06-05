package server_interaction;

import com.google.gson.Gson;
import general_classes.MessageCreator;
import javafx.application.Platform;
import server_interaction.Threads.WriteThread;

import java.io.*;

import static general_classes.Main.*;

/**
 * Created by Yunicoed on 31.05.2017.
 */
public class IOFuncs {
    private DataInputStream dIn;
    private DataOutputStream dOut;

    public IOFuncs(DataInputStream dIn, DataOutputStream dOut) {
        this.dIn = dIn;
        this.dOut = dOut;
    }

    public void getFirstData() {
        MessageSolver mSolver = new MessageSolver();
        MessageCreator mCreator = new MessageCreator();
        System.out.println("Getting start-Data...");
        Thread t = new WriteThread(toServer.getConnector(), mSolver.serializePacketOfData(mCreator.firstRead()));

        t.start();
        try {
            readFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addProject(String nameOfProject) {
        MessageSolver mSolver = new MessageSolver();
        MessageCreator mCreator = new MessageCreator();
        System.out.println("Sending project...");
        Thread t = new WriteThread(toServer.getConnector(), mSolver.serializePacketOfData(mCreator.addProject(nameOfProject)));
        t.start();
        try {
            readFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Read, write methods*/
    synchronized public void writeToServer(String sms) {
        try {
            System.out.println("Writing to server...");
            dOut.writeUTF(sms);
            dOut.flush();
        } catch (IOException e) {
            System.out.println("Caused problem in writing to server");
        }
    }

    public void readFromServer() throws IOException {
        String line = null;
        line = dIn.readUTF();
        System.out.println(line);
        PacketOfData packetOfData = new Gson().fromJson(line, PacketOfData.class);
        dataHolder.setProjects(packetOfData.getProjectsList());
        //dataHolder.showAllProjects();
        mainController.putDataToObservableList();
    }

    public void awaitOfUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean allIsGood = true;
                while (allIsGood) {
                    try {
                        readFromServer();
                    } catch (IOException e) {
                        allIsGood = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                fromServer.getConnector().showLostConnection();
                            }
                        });
                    }
                    System.out.println("Wow, new Information");
                }
            }
        }).start();

    }

    /*Getters and setters*/
    public DataInputStream getdIn() {
        return dIn;
    }

    public DataOutputStream getdOut() {
        return dOut;
    }
}