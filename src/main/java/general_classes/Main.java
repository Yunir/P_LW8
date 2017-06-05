package general_classes;

import controllers.ConnectController;
import controllers.ServerUnavailableController;
import objects.DataHolder;
import objects.Project;
import server_interaction.Threads.LogInThread;
import controllers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends Application {
    public static DataHolder dataHolder;
    public static ToServer toServer;
    public static FromServer fromServer;
    //Controllers
    private ControllerCreator cc = new ControllerCreator();
    public static MainController mainController;
    public static ConnectController connectController;
    public static ServerUnavailableController serverUnavailableController;

    public static volatile ReentrantLock locker = new ReentrantLock();
    public static volatile Condition accessToResource = locker.newCondition();

    public static void main(String[] args)  {
        dataHolder = new DataHolder();
        toServer = new ToServer();
        fromServer = new FromServer();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        //create Controllers
        mainController = cc.showMainView(primaryStage);
        connectController = cc.showLogInDialog(primaryStage);
        serverUnavailableController = cc.prepareServerUnavailableDialog(primaryStage);
        if(toServer.establishConnection(primaryStage)) {
            fromServer.establishConnection(primaryStage);
            fromServer.awaitOfUpdates();
            toServer.getFirstFullPacket();
            LogInThread logIn = new LogInThread();
            logIn.start();
        }
    }


}
