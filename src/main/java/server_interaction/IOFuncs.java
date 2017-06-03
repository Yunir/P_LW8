package server_interaction;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import main.Main;
import objects.Project;
import server_interaction.Threads.WriteThread;

import java.io.*;
import java.net.SocketException;

import static main.Main.*;

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

    public static void getFirstData() {
        MessageSolver mSolver = new MessageSolver();
        MessageCreator mCreator = new MessageCreator();
        System.out.println("Getting start-Data...");
        Thread t = new WriteThread(mSolver.serializePacketOfData(mCreator.firstRead()));
        t.start();
        try {IOConnector.ioFuncs.readFromServer();} catch (IOException e) {e.printStackTrace();}

    }

    public static void addProject(String nameOfProject) {
        MessageSolver mSolver = new MessageSolver();
        MessageCreator mCreator = new MessageCreator();
        System.out.println("Sending project...");
        Thread t = new WriteThread(mSolver.serializePacketOfData(mCreator.addProject(nameOfProject)));
        t.start();
        try {IOConnector.ioFuncs.readFromServer();} catch (IOException e) {e.printStackTrace();}
    }

    /*Read, write methods*/
    synchronized public void writeToServer(String sms){
        try {
            System.out.println("Writing to server...");
            dOut.writeUTF(sms);
            dOut.flush();
        } catch (IOException e) { System.out.println("Caused problem in writing to server"); }
    }
    public void readFromServer() throws SocketException, IOException {
        String line = null;
        line = dIn.readUTF();
        System.out.println(line);
        PacketOfData packetOfData = new Gson().fromJson(line, PacketOfData.class);
        data.setProjects(packetOfData.getProjectsList());
        //data.showAllProjects();
        mainController.putDataToObservableList();
    }

    public void awaitOfUpdates() {
        boolean allIsGood = true;
        while (allIsGood){
            try {acceptChangesConnector.ioFuncs.readFromServer();} catch (IOException e) {
                allIsGood = false;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        acceptChangesConnector.showLostConnection();
                    }
                });
            }
            System.out.println("Wow, new Information");
        }
    }

    /*Getters and setters*/
    public DataInputStream getdIn() {
        return dIn;
    }
    public DataOutputStream getdOut() {
        return dOut;
    }
}
