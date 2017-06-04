package general_classes;

import controllers.ConnectController;
import controllers.ServerUnavailableController;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import objects.DataHolder;
import server_interaction.Threads.LogInThread;
import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends Application {
    public static DataHolder data;
    public static ToServer toServer;
    public static FromServer fromServer;
    //Controllers
    ControllerCreator CC = new ControllerCreator();
    public static MainController mainController;
    public static ConnectController connectController;
    public static ServerUnavailableController serverUnavailableController;

    public static volatile ReentrantLock locker = new ReentrantLock();
    public static volatile Condition accessToResource = locker.newCondition();

    public static void main(String[] args)  {
        data = new DataHolder();
        toServer = new ToServer();
        fromServer = new FromServer();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        //create Controllers
        mainController = CC.showMainView(primaryStage);
        connectController = CC.showLogInDialog(primaryStage);
        serverUnavailableController = CC.prepareServerUnavailableDialog(primaryStage);
        if(toServer.establishConnection(primaryStage)) {
            fromServer.establishConnection(primaryStage);
            fromServer.awaitOfUpdates();
            toServer.getFirstFullPacket();
            LogInThread logIn = new LogInThread();
            logIn.start();
        }
    }
}
