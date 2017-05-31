package server_interaction;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * Created by Yunicoed on 17.05.2017.
 */
public class Connector {
    Socket socket = null;
    InputStream in = null;
    OutputStream out = null;
    private InetAddress IA;
    private int port;
    public IOFuncs ioFuncs;

    public void establishConnection() {
        try {
            socket = new Socket(IA, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            ioFuncs = new IOFuncs(new DataInputStream(in), new DataOutputStream(out));
        } catch (UnknownHostException e) {
            System.out.println("Host not found");
        } catch (IOException e) {
            //TODO: notify user
            System.out.println("Server is not available");
        }
    }

    /*Getters and setters*/

    public void setIA(InetAddress IA) {
        this.IA = IA;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
