package client_interaction;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ReceiveChangesServer extends Thread {
    private static final int RECIEVE_CHANGES_PORT = 8888;
    private ServerSocket serverRecieveChangesSocket = null;
    int countOfConnections = 0;



    public ReceiveChangesServer() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("localhost");
            serverRecieveChangesSocket = new ServerSocket(RECIEVE_CHANGES_PORT, 0, inetAddress);
            System.out.println("ReceiveChangesServer started");
        } catch (UnknownHostException e) {e.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}

    }

    public void waitConnections() {
        try {
            while(true) {
                Socket socket = null;
                ReceiveChangesConnection connection = null;
                try {
                    socket = serverRecieveChangesSocket.accept();
                    connection = new ReceiveChangesConnection(countOfConnections++, socket);
                } catch (IOException e) {e.printStackTrace();}
            }
        } finally {
            try {
                if (serverRecieveChangesSocket != null) serverRecieveChangesSocket.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    }
}
