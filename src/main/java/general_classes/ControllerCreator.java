package general_classes;

import controllers.ConnectController;
import controllers.MainController;
import controllers.ServerUnavailableController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ControllerCreator {

    /*Create Controllers*/
    MainController showMainView(Stage parent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles.Locale", new Locale("ru")));
            Parent root = loader.load();
            parent.setTitle(loader.getResources().getString("main.title"));
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
    ConnectController showLogInDialog(Stage parent) {
        Stage logInStage = new Stage(StageStyle.TRANSPARENT);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/connect.fxml"));
            Parent root = loader.load();
            logInStage.setResizable(false);
            logInStage.setScene(new Scene(root));
            logInStage.initModality(Modality.WINDOW_MODAL);
            logInStage.initOwner(parent);
            logInStage.show();
            ConnectController.stage = logInStage;
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    ServerUnavailableController prepareServerUnavailableDialog(Stage parent) {
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/serverUnavailable.fxml"));
            Parent root = loader.load();
            stage.setMinHeight(150);
            stage.setMinWidth(300);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parent);
            ServerUnavailableController.stage = stage;
            return loader.getController();
        } catch (IOException e) {
            System.out.println("Can'readThread load fxml 'connect' file: " + e.getMessage());
            return null;
        }
    }
}
