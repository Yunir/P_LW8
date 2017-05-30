package main;

import connection.Connector;
import clientside.ThreadToRead;
import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import static java.lang.Thread.sleep;

public class Main extends Application {
    public static boolean start = false;
    public static boolean refresh = false;
    public static ThreadToRead t;

    private static MainController MainController;
    public static Connector connector;
    public static DataModel data;

    @Override
    public void start(Stage primaryStage) throws IOException{
        MainController = setMainView(primaryStage);
        MainController.showLogInDialog(primaryStage);
    }


    public static void main(String[] args)  {
        data = new DataModel();
        try {
            connector = new Connector();
            connector.setIA(InetAddress.getByName("localhost"));
            connector.setPort(9999);
            connector.establishConnection();
            Thread k = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!start) {System.out.println(".");
                        try {
                            sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }}
                    t = new ThreadToRead(connector);
                    t.start();
                    refresh = true;
                }
            });
            k.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        launch(args);

    }

    private MainController setMainView(Stage parent) throws IOException {
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
