package server_interaction;

import com.google.gson.Gson;
import javafx.application.Platform;
import server_interaction.Threads.WriteThread;

import java.io.*;
import java.net.SocketException;

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

    public void getFirstData(Thread waitIt) {
        MessageSolver mSolver = new MessageSolver();
        MessageCreator mCreator = new MessageCreator();
        System.out.println("Getting start-Data...");
        Thread t = new WriteThread(toServer.getConnector(), mSolver.serializePacketOfData(mCreator.firstRead()));

        t.start();
        try {readFromServer();} catch (IOException e) {e.printStackTrace();}

    }

    public static void addProject(String nameOfProject) {
        MessageSolver mSolver = new MessageSolver();
        MessageCreator mCreator = new MessageCreator();
        System.out.println("Sending project...");
        Thread t = new WriteThread(toServer.getConnector(), mSolver.serializePacketOfData(mCreator.addProject(nameOfProject)));
        t.start();
        try {toServer.getConnector().ioFuncs.readFromServer();} catch (IOException e) {e.printStackTrace();}
    }

    /*Read, write methods*/
    synchronized public void writeToServer(String sms){
        try {
            System.out.println("Writing to server...");
            dOut.writeUTF(sms);
            dOut.flush();
        } catch (IOException e) { System.out.println("Caused problem in writing to server"); }
    }
    public void readFromServer() throws IOException {
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
            try {fromServer.getConnector().ioFuncs.readFromServer();} catch (IOException e) {
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

    /*Getters and setters*/
    public DataInputStream getdIn() {
        return dIn;
    }
    public DataOutputStream getdOut() {
        return dOut;
    }
}
