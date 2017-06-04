package client_interaction;

import data_processing.Processor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;

public class UserConnection extends Thread {
    Processor processor;
    private String TEMPL_MSG  = "The client '%d' sent me message: ";
    private String TEMPL_CONN = "The client '%d' closed the connection";
    DataInputStream dis;
    DataOutputStream dos;
    int idOfConnection;
    Socket IOSocket = null;

    public UserConnection(int num, Socket socket) {
        processor = new Processor();
        idOfConnection = num;
        this.IOSocket = socket;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        System.out.println("Client accepted");
        start();
    }

    public void run()
    {
        try {
            dis = new DataInputStream (IOSocket.getInputStream());
            dos = new DataOutputStream(IOSocket.getOutputStream());
            boolean haveAccess = false;

            String line = null;
            while(true) {
                // Ожидание сообщения от клиента
                while (!haveAccess) {
                    haveAccess = processor.checkLogPass(dis.readUTF());
                    System.out.println("Checking log-pass...");
                    if (haveAccess) {
                        dos.writeUTF("allow");
                        System.out.println("Send confirmation");
                    } else dos.writeUTF("deny");
                }
                line = dis.readUTF();
                System.out.println(String.format(TEMPL_MSG, idOfConnection) + line);
                //TODO: write that all is good and user can change data as he planned. Something like 'DONE'
                line = processor.analyzeMessage(line, idOfConnection);
                System.out.println("I'm sending back: " + line);
                dos.writeUTF(line);
                dos.flush();
                if (line.equalsIgnoreCase("quit")) {
                    //TODO: client close program and send last message 'quit'
                    IOSocket.close();
                    System.out.println(String.format(TEMPL_CONN, idOfConnection));
                    break;
                }
            }
        } catch(Exception e) {
            System.out.println("Exception : " + e);
        }
    }
}
