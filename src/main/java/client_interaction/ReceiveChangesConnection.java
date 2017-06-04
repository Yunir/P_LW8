package client_interaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data_processing.MessageSolver;
import data_processing.Processor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static main.Main.generalPacketOfData;
import static main.Main.notifyEveryone;
import static main.Main.temp;

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
                    synchronized (generalPacketOfData) {
                        try {while(!notifyEveryone) {
                        System.out.println("nothing to update to user " + idOfConnection);
                        generalPacketOfData.wait();
                        System.out.println("User is awake " + idOfConnection);
                    }
                    System.out.println("Write new information to user " + idOfConnection);
                    //generalPacketOfData = temp;
                    dos.writeUTF(gson.toJson(temp));} catch(Exception e){
                            System.out.println("some exception in notifying others");
                        }
                    dos.flush();
                }
                    sleep(5000);
                    notifyEveryone = false;
            }

                // Ожидание сообщения от клиента
             /*
                line = dis.readUTF();
                System.out.println(String.format(TEMPL_MSG, idOfConnection) + line);
                //TODO: write that all is good and user can change data as he planned. Something like 'DONE'
                line = processor.analyzeMessage(line);
                System.out.println("I'm sending back: " + line);
                dos.writeUTF(line);
                dos.flush();
                if (line.equalsIgnoreCase("quit")) {
                    //TODO: client close program and send last message 'quit'
                    recieveChangesSocket.close();
                    System.out.println(String.format(TEMPL_CONN, idOfConnection));
                    break;
                }
            }*/
        } catch(Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}
