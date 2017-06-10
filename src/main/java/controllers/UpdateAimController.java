package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;

import static general_classes.Main.toServer;

public class UpdateAimController {
    public static String oldAimName;
    public static String projectName;
    @FXML
    public static Stage UpdateAimStage;
    @FXML
    public TextField newAimName;
    @FXML
    public ComboBox newAimPriorityBox;
    @FXML
    public void initialize() {
        newAimPriorityBox.getItems().add(4);
        newAimPriorityBox.getItems().add(3);
        newAimPriorityBox.getItems().add(2);
        newAimPriorityBox.getItems().add(1);
        newAimPriorityBox.getSelectionModel().selectFirst();
    }
    public void updateAim(ActionEvent actionEvent) {
        Matcher matcher = MainController.pattern.matcher(newAimName.getText());
        if(matcher.matches() && newAimName.getText().toString().length() < 33) {
            toServer.getConnector().actionEventSolver.updateAim(projectName, oldAimName, newAimName.getText(), Integer.parseInt(newAimPriorityBox.getValue().toString()));
            UpdateAimStage.close();
        }
    }
}
