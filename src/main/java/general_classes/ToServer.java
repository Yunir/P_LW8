package general_classes;

import controllers.MainController;
import interfaces.ServerInterface;
import javafx.stage.Stage;
import server_interaction.Connector;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static general_classes.Main.accessToResource;
import static general_classes.Main.locker;

public class ToServer implements ServerInterface {
    private Connector connector;
    public ToServer() {
        defaultSettings();
    }

    /*public methods*/
    @Override
    public boolean establishConnection(Stage parent) {
        return connector.establishConnection(parent, false);
    }
    public void getFirstFullPacket() {
        connector.actionEventSolver.getFirstFullPacket();
    }

    /*private methods*/
    private void defaultSettings() {
        try {
            connector = new Connector(InetAddress.getByName("localhost"), 9999);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    /*Getters*/
    public Connector getConnector() {
        return connector;
    }

}
