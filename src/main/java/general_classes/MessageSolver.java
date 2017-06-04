package general_classes;

import client_interaction.PacketOfData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import objects.Project;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static general_classes.Main.*;

public class MessageSolver {
    private final String LOGIN = "admin";
    private final String PASSWORD = DigestUtils.md5Hex("admin");
    private final String REQUEST_ACCEPT = "accept";
    private final String REQUEST_DENY = "deny";
    private final String WRITE_MESSAGE = "The user %d sent me message: \n%s\n\n";
    private Gson gson;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int userId;

    public MessageSolver(DataInputStream dis, DataOutputStream dos, int userId) {
        this.dis = dis;
        this.dos = dos;
        this.userId = userId;
        gson = new GsonBuilder().create();
    }

    public boolean checkAuthData() {
        try {
            String authData = dis.readUTF();
            String[] splitedLine = authData.split(";");
            while (true){
                if (LOGIN.equals(splitedLine[0]) && PASSWORD.equals(splitedLine[1])) {
                    dos.writeUTF(REQUEST_ACCEPT);
                    return true;
                }
                else dos.writeUTF(REQUEST_DENY);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void startConversationWithUser() {
        try {
            String receivedMessage;
            while(true) {
                receivedMessage = dis.readUTF();
                System.out.printf(WRITE_MESSAGE, userId, receivedMessage);
                dos.writeUTF(analyzeMessage(receivedMessage, userId));
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
    }
    }

    public String analyzeMessage(String message, int idOfConnection) {
        PacketOfData packetOfData = gson.fromJson(message, PacketOfData.class);
        switch (packetOfData.getCommandType()) {
            case FIRST_READ:
                packetOfData.setProjectsList(dataHolder.getProjectsList());
                return gson.toJson(packetOfData);
            case ADD_PROJECT:
                locker.lock();
                //add to collection
                dataHolder.getProjectsList().add(new Project(packetOfData.getName(), 0));
                //add to DB
                DB.insertProject(packetOfData.getName());
                generalPacketOfData = new PacketOfData();
                System.out.println("Need to send new data to other users");
                notifyEveryone = true;
                generalPacketOfData.projectsList = dataHolder.getProjectsList();
                generalPacketOfData.setConnectionId(idOfConnection);
                updates.signalAll();
                System.out.println("Notified everyone, that we have new Data");
                locker.unlock();
                return REQUEST_ACCEPT;
            default:
                break;
        }
        return null;
    }
}
