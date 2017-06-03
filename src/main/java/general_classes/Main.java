package general_classes;

import controllers.ConnectController;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import objects.DataHolder;
import server_interaction.Connector;
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
    public static ToServer toServer;
    public static FromServer fromServer;
    public static MainController mainController;
    public static DataHolder data;
    public static ReentrantLock locker = new ReentrantLock();
    public static Condition condition = locker.newCondition();
    public static void main(String[] args)  {
        data = new DataHolder();
        toServer = new ToServer();
        fromServer = new FromServer();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        mainController = showMainView(primaryStage);
        mainController.showLogInDialog(primaryStage);
        mainController.initializeDataObsLists();
        if(toServer.establishConnection(primaryStage)) {
            //fromServer.establishConnection(primaryStage);
            LogInThread logIn = new LogInThread();
            logIn.start();
            //TODO: awaiting thread
            //toServer.runThread(logIn);
            toServer.getFirstFullPacket(logIn);
        }
    }

    /*Create Controllers*/
    private MainController showMainView(Stage parent) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"));
        Parent root = loader.load();
        parent.setTitle("Deal with it!");
        parent.setMinWidth(650);
        parent.setMinHeight(650);
        parent.setScene(new Scene(root));
        parent.show();
        return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private ConnectController showLogInDialog(Stage parent) {
        Stage logInStage = new Stage(StageStyle.TRANSPARENT);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/connect.fxml"));
            Parent root = loader.load();
            logInStage.setResizable(false);
            logInStage.setScene(new Scene(root));
            logInStage.initModality(Modality.WINDOW_MODAL);
            logInStage.initOwner(parent);
            logInStage.show();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
