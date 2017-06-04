package client_interaction;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class IOServer extends Thread {
    private static final int IO_PORT = 9999;
    private ServerSocket serverIOSocket = null;
    int countOfConnections = 0;

    public IOServer() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("localhost");
            serverIOSocket = new ServerSocket(IO_PORT, 0, inetAddress);
            System.out.println("IOServer started");
        } catch (UnknownHostException e) {e.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}

    }

    public void waitConnections() {
        try {
            while(true) {
                Socket socket = null;
                UserConnection connection = null;
                try {
                    socket = serverIOSocket.accept();
                    connection = new UserConnection(countOfConnections++, socket);
                } catch (IOException e) {e.printStackTrace();}
            }
        } finally {
            try {
                if (serverIOSocket != null) serverIOSocket.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    }
}
