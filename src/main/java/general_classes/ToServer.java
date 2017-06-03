package general_classes;

import controllers.MainController;
import interfaces.ServerInterface;
import javafx.stage.Stage;
import server_interaction.Connector;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static general_classes.Main.condition;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                locker.lock();
                System.out.println("lock: ToServer");
                try {
                    if(!MainController.confirmationReceived)condition.await();
                    System.out.println("Start downloading FirstData");
                    connector.getIoFuncs().getFirstData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    locker.unlock();
                }
            }
        }).start();


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
