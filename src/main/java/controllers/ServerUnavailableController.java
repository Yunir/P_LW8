package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ServerUnavailableController {
    @FXML
    public static Stage stage;
    public void exitTheApp(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void showServerUnavailableScene() {
        stage.show();
    }
}
