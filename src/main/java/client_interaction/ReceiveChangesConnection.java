package client_interaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data_processing.Processor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static general_classes.Main.*;

public class ReceiveChangesConnection extends Thread {
    Processor processor;
    private String TEMPL_MSG  = "The client '%d' sent me message: ";
    private String TEMPL_CONN = "The client '%d' closed the connection";
    DataInputStream dis;
    DataOutputStream dos;
    int idOfConnection;
    Socket recieveChangesSocket = null;
    Gson gson;

    public ReceiveChangesConnection(int num, Socket socket) {
        processor = new Processor();
        gson = new GsonBuilder().create();
        idOfConnection = num;
        this.recieveChangesSocket = socket;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        System.out.println("Client accepted");
        start();
    }

    public void run()
    {
        try {
            dis = new DataInputStream (recieveChangesSocket.getInputStream());
            dos = new DataOutputStream(recieveChangesSocket.getOutputStream());

                while (true) {
                    locker.lock();
                    try {
                        while(!notifyEveryone) {
                            System.out.println("nothing to update to user " + idOfConnection);
                            updates.await();
                            System.out.println("User is awake " + idOfConnection);
                        }
                        System.out.println("Write new information to user " + idOfConnection);
                        if(idOfConnection != generalPacketOfData.getConnectionId()) dos.writeUTF(gson.toJson(generalPacketOfData));
                        else System.out.println("User " + idOfConnection + " sent this updates, he doesn't need on it");
                    }
                    catch(Exception e){
                            System.out.println("some exception in notifying others");
                    }
                    dos.flush();
                    locker.unlock();
                sleep(5000);
                notifyEveryone = false;
             }
        } catch(Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}
