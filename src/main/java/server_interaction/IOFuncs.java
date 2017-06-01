package server_interaction;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import javafx.collections.FXCollections;
import main.Main;

import java.io.*;

import static main.Main.data;

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

    synchronized public void writeToServer(String sms){
        //InputStreamReader isr = new InputStreamReader(System.in);
        //BufferedReader keyboard = new BufferedReader(isr);
        try {
            dOut.writeUTF(sms);
            dOut.flush();
        } catch (IOException e) { System.out.println("Caused problem in writing to server"); }
    }

    public void readFromServer(){
        String line = null;
        try {
            while(true){
                //if(con_established) {}
                line = dIn.readUTF();
                    System.out.println(";");
                    System.out.println(line);
                    PacketOfData packetOfData = new Gson().fromJson(line, PacketOfData.class);
                    //data.setProjects(packetOfData.getProjectsList());

                    Main.readThread.await_of_collection = false;
                    System.out.println("Installed" + Main.readThread.await_of_collection + " value");
            }
        } catch (IOException e) {
            System.out.println("Something incorrect in listenToServer");
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
