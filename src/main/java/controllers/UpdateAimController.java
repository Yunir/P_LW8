package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static general_classes.Main.toServer;

public class UpdateAimController {
    public static String oldAimName;
    public static String projectName;
    @FXML
    public static Stage UpdateAimStage;
    @FXML
    public TextField newAimName;
    @FXML
    public TextField newAimPriority;

    public void updateAim(ActionEvent actionEvent) {
        toServer.getConnector().actionEventSolver.updateAim(projectName, oldAimName, newAimName.getText(), Integer.parseInt(newAimPriority.getText()));
        UpdateAimStage.close();
    }
}
