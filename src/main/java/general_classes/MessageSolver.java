package general_classes;

import client_interaction.PacketOfData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import objects.Aim;
import objects.Project;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;

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

    public boolean checkAuthData() throws IOException {
        while (true){
            System.out.println("I am waiting to catch it");
            String authData = dis.readUTF();
            System.out.println(authData + " gotten");
            String[] splitedLine = authData.split(";");
            if (LOGIN.equals(splitedLine[0]) && PASSWORD.equals(splitedLine[1])) {
                dos.writeUTF(REQUEST_ACCEPT);
                return true;
            }
            else dos.writeUTF(REQUEST_DENY);
        }
    }
    
    public void startConversationWithUser() throws IOException {
        String receivedMessage;
        while(true) {

            /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while(dis.available() != 0) {
                System.out.print("here");
                baos.write(dis.readByte());
            }
            receivedMessage = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            System.out.println("Recieved message " + receivedMessage);*/
            receivedMessage = dis.readUTF();

            System.out.printf(WRITE_MESSAGE, userId, receivedMessage);
            dos.writeUTF(analyzeMessage(receivedMessage, userId));
            dos.flush();
        }
    }

    public String analyzeMessage(String message, int idOfConnection) {
        PacketOfData packetOfData = gson.fromJson(message, PacketOfData.class);
        switch (packetOfData.getCommandType()) {
            case FIRST_READ:
                packetOfData.setProjectsList(dataHolder.getProjectsList());
                return gson.toJson(packetOfData);
            case ADD_PROJECT:
                boolean everyoneUnique = true;
                String[] ssplitedLine = packetOfData.getName().split(";");
                for (int i = 0; i < ssplitedLine.length; i++) {
                    if(DB.findSimilarProjects(ssplitedLine[i]) != 0){
                        everyoneUnique = false;
                        break;
                    }
                }
                if(everyoneUnique){
                    locker.lock();
                    //add to DB
                    DB.insertProject(ssplitedLine);
                    //add to collection
                    for (int i = 0; i < ssplitedLine.length; i++) {
                        dataHolder.getProjectsList().add(new Project(ssplitedLine[i], 0, OffsetDateTime.now()));
                    }
                    generalPacketOfData = new PacketOfData();
                    System.out.println("Need to send new data to other users");
                    notifyEveryone = true;
                    generalPacketOfData.projectsList = dataHolder.getProjectsList();
                    generalPacketOfData.setConnectionId(idOfConnection);
                    updates.signalAll();
                    System.out.println("Notified everyone, that we have new Data");
                    locker.unlock();
                    return REQUEST_ACCEPT;
                } else {
                    System.out.println("You have already project with name " + packetOfData.getName());
                    return REQUEST_DENY;
                }
            case UPDATE_PROJECT:
                String[] splitedLine = packetOfData.getName().split(";");
                if(DB.findSimilarProjects(splitedLine[1]) == 0){
                    locker.lock();
                    //change in collection
                    for (Project eachProject : dataHolder.getProjectsList()) {
                        if (eachProject.getName().equals(splitedLine[0])) {
                            eachProject.setName(splitedLine[1]);
                            break;
                        }
                    }
                    //update in DB
                    DB.updateProject(splitedLine[0], splitedLine[1]);
                    generalPacketOfData = new PacketOfData();
                    System.out.println("Need to send new data to other users");
                    notifyEveryone = true;
                    generalPacketOfData.projectsList = dataHolder.getProjectsList();
                    generalPacketOfData.setConnectionId(idOfConnection);
                    updates.signalAll();
                    System.out.println("Notified everyone, that we have new Data");
                    locker.unlock();
                    return REQUEST_ACCEPT;
                } else {
                    System.out.println("You have already project with name " + splitedLine[1]);
                    return REQUEST_DENY;
                }
            case DELETE_PROJECT:
                if(DB.findSimilarProjects(packetOfData.getName()) != 0){
                    locker.lock();
                    //delete in collection
                    int deleteNum = -1;
                    for (int i = 0; i < dataHolder.getProjectsList().size(); i++) {
                        if(dataHolder.getProjectsList().get(i).getName().equals(packetOfData.getName())) {
                            deleteNum = i;
                            break;
                        }
                    }
                    dataHolder.getProjectsList().remove(deleteNum);
                    //delete from DB
                    DB.deleteProject(packetOfData.getName());
                    generalPacketOfData = new PacketOfData();
                    System.out.println("Need to send new data to other users");
                    notifyEveryone = true;
                    generalPacketOfData.projectsList = dataHolder.getProjectsList();
                    generalPacketOfData.setConnectionId(idOfConnection);
                    updates.signalAll();
                    System.out.println("Notified everyone, that we have new Data");
                    locker.unlock();
                    return REQUEST_ACCEPT;
                } else {
                    System.out.println("You don't have project with name " + packetOfData.getName());
                    return REQUEST_DENY;
                }
            case ADD_AIM:
                String[] splitedLineAim = packetOfData.getName().split(";");
                if(DB.findSimilarAims(splitedLineAim[0], splitedLineAim[1]) == 0){
                    locker.lock();
                    //change in collection
                    for (Project eachProject : dataHolder.getProjectsList()) {
                        if (eachProject.getName().equals(splitedLineAim[0])) {
                            eachProject.getAimsList().add(new Aim(splitedLineAim[1], packetOfData.getPriority(), OffsetDateTime.now()));
                            eachProject.setAmount(eachProject.getAmount()+1);
                            break;
                        }
                    }
                    //update in DB
                    DB.addAim(splitedLineAim[0], splitedLineAim[1], packetOfData.getPriority());
                    generalPacketOfData = new PacketOfData();
                    System.out.println("Need to send new data to other users");
                    notifyEveryone = true;
                    generalPacketOfData.projectsList = dataHolder.getProjectsList();
                    generalPacketOfData.setConnectionId(idOfConnection);
                    updates.signalAll();
                    System.out.println("Notified everyone, that we have new Data");
                    locker.unlock();
                    return REQUEST_ACCEPT;
                } else {
                    System.out.println("You have already aim with same name - " + splitedLineAim[1]);
                    return REQUEST_DENY;
                }
            case UPDATE_AIM:
                String[] updtAim = packetOfData.getName().split(";");
                if(DB.findSimilarAims(updtAim[0], updtAim[1]) != 0){
                    locker.lock();
                    //change in collection
                    for (int i = 0; i < dataHolder.getProjectsList().size(); i++) {
                        if(dataHolder.getProjectsList().get(i).getName().equals(updtAim[0])) {
                            for (int j = 0; j < dataHolder.getProjectsList().get(i).getAimsList().size(); j++) {
                                if(dataHolder.getProjectsList().get(i).getAimsList().get(j).getName().equals(updtAim[1])) {
                                    dataHolder.getProjectsList().get(i).getAimsList().get(j).setName(updtAim[2]);
                                    dataHolder.getProjectsList().get(i).getAimsList().get(j).setPriority(packetOfData.getPriority());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    //update in DB
                    DB.updateAim(updtAim[0], updtAim[1], updtAim[2], packetOfData.getPriority());
                    generalPacketOfData = new PacketOfData();
                    System.out.println("Need to send new data to other users");
                    notifyEveryone = true;
                    generalPacketOfData.projectsList = dataHolder.getProjectsList();
                    generalPacketOfData.setConnectionId(idOfConnection);
                    updates.signalAll();
                    System.out.println("Notified everyone, that we have new Data");
                    locker.unlock();
                    return REQUEST_ACCEPT;
                } else {
                    System.out.println("You have already aim with same name - " + updtAim[1]);
                    return REQUEST_DENY;
                }
            case DELETE_AIM:
                String[] delAim = packetOfData.getName().split(";");
                if(DB.findSimilarAims(delAim[0], delAim[1]) != 0){
                    locker.lock();
                    //change in collection
                    for (int i = 0; i < dataHolder.getProjectsList().size(); i++) {
                        if(dataHolder.getProjectsList().get(i).getName().equals(delAim[0])) {
                            for (int j = 0; j < dataHolder.getProjectsList().get(i).getAimsList().size(); j++) {
                                if(dataHolder.getProjectsList().get(i).getAimsList().get(j).getName().equals(delAim[1])) {
                                    dataHolder.getProjectsList().get(i).getAimsList().remove(j);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    //update in DB
                    DB.deleteAim(delAim[0], delAim[1]);
                    generalPacketOfData = new PacketOfData();
                    System.out.println("Need to send new data to other users");
                    notifyEveryone = true;
                    generalPacketOfData.projectsList = dataHolder.getProjectsList();
                    generalPacketOfData.setConnectionId(idOfConnection);
                    updates.signalAll();
                    System.out.println("Notified everyone, that we have new Data");
                    locker.unlock();
                    return REQUEST_ACCEPT;
                } else {
                    System.out.println("You don't have aim with same name - " + delAim[1]);
                    return REQUEST_DENY;
                }
            case CREATE_OBJECT_TABLE:
                if(packetOfData.getPriority() == 2)
                    DB.newObjectTable(Aim.class);
                else DB.newObjectTable(Project.class);
                return REQUEST_ACCEPT;
            default:
                break;
        }
        return null;
    }

    public void write(PacketOfData p) {
        try {
            dos.writeUTF(gson.toJson(p));
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
