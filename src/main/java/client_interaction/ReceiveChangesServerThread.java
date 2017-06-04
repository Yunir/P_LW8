package client_interaction;

public class ReceiveChangesServerThread extends Thread {
    @Override
    public void run() {
        ReceiveChangesServer recieveChangesServer = new ReceiveChangesServer();
        recieveChangesServer.waitConnections();
    }
}
