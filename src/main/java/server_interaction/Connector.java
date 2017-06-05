package server_interaction;

import general_classes.ActionEventSolver;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

import static general_classes.Main.serverUnavailableController;

public class Connector {
    Socket socket = null;
    InputStream in = null;
    OutputStream out = null;

    private InetAddress IA;
    private int port;
    public ActionEventSolver actionEventSolver;
    Stage primaryStage;

    public Connector(InetAddress IA, int port) {
        this.IA = IA;
        this.port = port;
    }

    public boolean establishConnection(Stage primaryStage, boolean awaiting) {
        this.primaryStage = primaryStage;
        try {
            socket = new Socket(IA, port);
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress(InetAddress.getByName("localhost"), port));
            actionEventSolver = new ActionEventSolver(new DataInputStream(Channels.newInputStream(sc)), new DataOutputStream(Channels.newOutputStream(sc)));
            /*if(awaiting){
                AwaitChangesThread awaitChangesThread = new AwaitChangesThread(ioFuncs);
                awaitChangesThread.start();
            }*/
            actionEventSolver = new ActionEventSolver(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.getOutputStream()));
            return true;
        } catch (UnknownHostException e) {
            System.out.println("Host not found");
            return false;
        } catch (SocketException e){
            serverUnavailableController.showServerUnavailableScene();
            System.out.println("Server in "+ IA.getHostAddress()+":"+port+" is not available");
            return false;
        } catch (IOException e) {
            //TODO MEM Do it!
            //TODO MEM Pismak - yes you can, смелее
            //TODO MEM you entered to secret zone (oy-yeah)
            serverUnavailableController.showServerUnavailableScene();
            System.out.println("Server in "+ IA.getHostAddress()+":"+port+" is not available");
            return false;
        }
    }

    public void showLostConnection () {
        serverUnavailableController.showServerUnavailableScene();
        System.out.println("Server in "+ IA.getHostAddress()+":"+port+" is not available");
    }

    /*Getters*//*
    public IOFuncs getIoFuncs() {
        return ioFuncs;
    }*/
}
