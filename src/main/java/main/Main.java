package main;

import objects.DataHolder;
import server_interaction.Connector;
import server_interaction.Threads.ReadThread;
import server_interaction.Threads.LogInThread;
import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server_interaction.Threads.RefreshThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main extends Application {

    public static MainController mainController;
    public static ReadThread readThread;
    public static Connector IOConnector;
    public static Connector acceptChangesConnector;
    public static DataHolder data;

    @Override
    public void start(Stage primaryStage) throws IOException{
        mainController = showMainView(primaryStage);
        mainController.showLogInDialog(primaryStage);
        mainController.initializeDataObsLists();

        if(IOConnector.establishConnection(primaryStage)) {
            //acceptChangesConnector.establishConnection();
            LogInThread logIn = new LogInThread();
            logIn.start();
            //TODO: awaiting thread
            readThread = new ReadThread(logIn);
            readThread.start();
            RefreshThread refreshThread = new RefreshThread(logIn);
            refreshThread.start();
        }
    }


    public static void main(String[] args)  {
        data = new DataHolder();
        try {
            IOConnector = new Connector();
            IOConnector.setIA(InetAddress.getByName("localhost"));
            IOConnector.setPort(9999);
            acceptChangesConnector = new Connector();
            acceptChangesConnector.setIA(InetAddress.getByName("localhost"));
            acceptChangesConnector.setPort(8888);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        launch(args);

    }

    private MainController showMainView(Stage parent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"));
        Parent root = loader.load();
        parent.setTitle("Deal with it!");
        parent.setMinWidth(650);
        parent.setMinHeight(650);
        parent.setScene(new Scene(root));
        parent.show();
        return loader.getController();
    }
}
