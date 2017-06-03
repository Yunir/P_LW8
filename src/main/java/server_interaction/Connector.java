package server_interaction;

import javafx.stage.Stage;
import server_interaction.Threads.AwaitChangesThread;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import static general_classes.Main.mainController;

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
    Stage primaryStage;

    public Connector(InetAddress IA, int port) {
        this.IA = IA;
        this.port = port;
    }

    public boolean establishConnection(Stage primaryStage, boolean awaiting) {
        this.primaryStage = primaryStage;
        try {
            socket = new Socket(IA, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            ioFuncs = new IOFuncs(new DataInputStream(in), new DataOutputStream(out));
            if(awaiting){
                AwaitChangesThread awaitChangesThread = new AwaitChangesThread(ioFuncs);
                awaitChangesThread.start();
            }
            return true;
        } catch (UnknownHostException e) {
            System.out.println("Host not found");
            return false;
        } catch (SocketException e){
            mainController.showServerUnavailableDialog(primaryStage);
            System.out.println("Server in "+ IA.getHostAddress()+":"+port+" is not available");
            return false;
        } catch (IOException e) {
            //TODO MEM pepe frog
            //TODO MEM Do it!
            //TODO MEM Pismak - yes you can, смелее
            //TODO MEM you entered to secret zone (oy-yeah)
            mainController.showServerUnavailableDialog(primaryStage);
            System.out.println("Server in "+ IA.getHostAddress()+":"+port+" is not available");
            return false;
        }
    }

    public void showLostConnection () {
        mainController.showServerUnavailableDialog(primaryStage);
        System.out.println("Server in "+ IA.getHostAddress()+":"+port+" is not available");
    }

    /*Getters*/
    public IOFuncs getIoFuncs() {
        return ioFuncs;
    }
}
