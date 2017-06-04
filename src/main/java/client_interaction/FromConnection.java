package client_interaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static general_classes.Main.*;

public class FromConnection extends Thread {
    DataInputStream dis;
    DataOutputStream dos;
    int idOfConnection;
    Socket recieveChangesSocket = null;
    Gson gson;

    public FromConnection(int num, Socket socket) {
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
                System.out.println(idOfConnection + ".");
                while(!notifyEveryone) {
                        System.out.println("nothing to update to user " + idOfConnection);
                        updates.await();
                    System.out.println(idOfConnection + "..");
                    System.out.println("User is awake " + idOfConnection);
                    }
                    System.out.println("Write new information to user " + idOfConnection);
                    if(idOfConnection != generalPacketOfData.getConnectionId()) dos.writeUTF(gson.toJson(generalPacketOfData));
                    else System.out.println("User " + idOfConnection + " sent this updates, he doesn't need on it");
                System.out.println(idOfConnection + "...");
                dos.flush();
                locker.unlock();
            sleep(1000);
                System.out.println(idOfConnection + "....");

                notifyEveryone = false;
                System.out.println(idOfConnection + ".....");

            }
        } catch(Exception e) {
            locker.unlock();
            try {
                System.out.println("Connection with User " + idOfConnection + " had lost.");
                recieveChangesSocket.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
    }
}
