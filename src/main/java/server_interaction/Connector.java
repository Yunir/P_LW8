package server_interaction;

import clientside.SendPacket;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import main.Main;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static main.Main.data;

/**
 * Created by Yunicoed on 17.05.2017.
 */
public class Connector {
    public boolean con_established = false;
    Socket socket = null;
    InputStream in = null;
    OutputStream out = null;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    private InetAddress IA;
    private int port;
    public IOFuncs ioFuncs;

    public void establishConnection() {
        try {
            socket = new Socket(IA, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            dIn = new DataInputStream(in);
            dOut = new DataOutputStream(out);
            ioFuncs = new IOFuncs(dIn, dOut);
            con_established = true;
        } catch (UnknownHostException e) {
            System.out.println("Host not found");
        } catch (IOException e) {
            //TODO: notify user
            System.out.println("Server is not available");
        }
    }



    public void listenToServer(){
        String line = null;
        try {
            while(true){
                if(con_established) {
                    line = dIn.readUTF();
                    System.out.println(";");
                    System.out.println(line);
                    SendPacket s = new Gson().fromJson(line, SendPacket.class);
                    data.getProjects().setProjectsObsList(FXCollections.observableArrayList());
                    data.getAims().setAimsObsList(FXCollections.observableArrayList());
                    for (int i = 0; i < s.projectsList.size(); i++) {
                        data.getProjects().create(s.projectsList.get(i));
                        //System.out.println(s.projectsList.get(i).getName());
                    }
                    for (int i = 0; i < s.aimsList.size(); i++) {
                        data.getAims().create(s.aimsList.get(i));
                    }
                    Main.readThread.await_of_collection = false;
                    System.out.println("Installed" + Main.readThread.await_of_collection + " value");
                }
            }
        } catch (IOException e) {
            System.out.println("Something incorrect in listenToServer");
        }
    }




    /*Getters and setters*/
    public InetAddress getIA() {
        return IA;
    }

    public void setIA(InetAddress IA) {
        this.IA = IA;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public DataInputStream getdIn() {
        return dIn;
    }
}
