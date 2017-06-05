package server_interaction;

import com.google.gson.Gson;
import javafx.application.Platform;

import java.io.*;

import static general_classes.Main.*;

public class IOFuncs {
    private DataInputStream dIn;
    private DataOutputStream dOut;

    public IOFuncs(DataInputStream dIn, DataOutputStream dOut) {
        this.dIn = dIn;
        this.dOut = dOut;
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

    /*public void awaitOfUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean allIsGood = true;
                while (allIsGood) {
                    try {
                        readFromServer();
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
    }*/

    /*Getters and setters*/
    public DataInputStream getdIn() {
        return dIn;
    }
}