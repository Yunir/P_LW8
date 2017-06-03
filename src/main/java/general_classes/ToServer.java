package general_classes;

import interfaces.ServerInterface;
import javafx.stage.Stage;
import server_interaction.Connector;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static general_classes.Main.condition;
import static general_classes.Main.locker;

public class ToServer implements ServerInterface {
    private Connector connector;
    //private ToServerThread toServerThread;


    public ToServer() {
        defaultSettings();
    }


    /*public methods*/
    @Override
    public boolean establishConnection(Stage parent) {
        return connector.establishConnection(parent, false);
    }
    public void getFirstFullPacket(Thread waitIt) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                locker.lock();
                try {
                    System.out.println("waiting logpass");
                    condition.await();
                    connector.getIoFuncs().getFirstData(waitIt);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    locker.unlock();
                }
            }
        }).start();


    }
    /*@Override
    public void runThread(Thread logIn) {
        toServerThread = new ToServerThread(logIn);
        toServerThread.start();
    }*/


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
