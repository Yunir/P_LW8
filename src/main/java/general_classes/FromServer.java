package general_classes;

import controllers.MainController;
import interfaces.ServerInterface;
import javafx.stage.Stage;
import server_interaction.Connector;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static general_classes.Main.accessToResource;
import static general_classes.Main.locker;

public class FromServer implements ServerInterface {
    private Connector connector;

    public FromServer() {
        defaultSettings();
    }


    /*public methods*/
    @Override
    public boolean establishConnection(Stage parent) {
        return connector.establishConnection(parent, false);
    }
    public void awaitOfUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                locker.lock();
                System.out.println("lock: FromServer");
                try {
                    if(!MainController.confirmationReceived) accessToResource.await();
                    System.out.println("проснулся: FromServer");

                    connector.getIoFuncs().awaitOfUpdates();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    locker.unlock();
                }
            }
        }).start();

    }


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
