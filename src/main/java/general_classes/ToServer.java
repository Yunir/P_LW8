package general_classes;

import client_interaction.ToConnection;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ToServer extends Thread {
    private final int IO_PORT = 9999;
    private InetAddress inetAddress;
    private ServerSocket serverSocket = null;
    private int countOfConnections;

    public ToServer() {
        try {
            countOfConnections = 0;
            inetAddress = InetAddress.getByName("localhost");
            serverSocket = new ServerSocket(IO_PORT, 0, inetAddress);
        } catch (UnknownHostException e) {e.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}
    }

    @Override
    public void run() {
        System.out.println("ToServer started");
        Socket socket;
        try {
            while(true) {
                socket = serverSocket.accept();
                new ToConnection(countOfConnections++, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
