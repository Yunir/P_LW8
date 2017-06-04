package general_classes;

import client_interaction.FromConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FromServer extends Thread {
    private final int PORT = 8888;
    private InetAddress inetAddress;
    private ServerSocket serverSocket = null;
    int countOfConnections = 0;

    public FromServer() {
        try {
            countOfConnections = 0;
            inetAddress = InetAddress.getByName("localhost");
            serverSocket = new ServerSocket(PORT, 0, inetAddress);
        } catch (IOException e) {e.printStackTrace();}
    }

    @Override
    public void run() {
        System.out.println("FromServer started");
        Socket socket;
        try {
            while(true) {
                socket = serverSocket.accept();
                new FromConnection(countOfConnections++, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
