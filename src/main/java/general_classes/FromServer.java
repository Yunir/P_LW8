package general_classes;

import interfaces.ServerInterface;
import javafx.stage.Stage;
import server_interaction.Connector;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class FromServer implements ServerInterface {
    private Connector connector;
    //private FromServerThread fromServerThread;

    public FromServer() {
        defaultSettings();
    }


    /*public methods*/
    @Override
    public boolean establishConnection(Stage parent) {
        return connector.establishConnection(parent, false);
    }
    /*@Override
    public void runThread(Thread logIn) {
        fromServerThread = new ToServerThread(logIn);
        fromServerThread.start();
    }*/


    /*private methods*/
    private void defaultSettings() {
        try {
            connector = new Connector(InetAddress.getByName("localhost"), 8888);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    /*Getters*/
    public Connector getConnector() {
        return connector;
    }
}
