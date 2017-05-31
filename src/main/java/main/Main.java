package main;

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
    public static Connector connector;
    public static DataModel data;

    @Override
    public void start(Stage primaryStage) throws IOException{
        mainController = showMainView(primaryStage);
        mainController.showLogInDialog(primaryStage);
    }


    public static void main(String[] args)  {
        data = new DataModel();
        try {
            connector = new Connector();
            connector.setIA(InetAddress.getByName("localhost"));
            connector.setPort(9999);
            connector.establishConnection();
            LogInThread logIn = new LogInThread();
            logIn.start();
            readThread = new ReadThread(logIn);
            readThread.start();
            RefreshThread refreshThread = new RefreshThread(logIn);
            refreshThread.start();
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
