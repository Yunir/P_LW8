package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;

import static general_classes.Main.toServer;

public class CreateAimController {
    public static String projectName;
    @FXML
    public static Stage CreateAimStage;
    @FXML
    public TextField aimName;
    @FXML
    public ComboBox aimPriorityBox;
    @FXML
    public void initialize() {
        aimPriorityBox.getItems().add(4);
        aimPriorityBox.getItems().add(3);
        aimPriorityBox.getItems().add(2);
        aimPriorityBox.getItems().add(1);
        aimPriorityBox.getSelectionModel().selectFirst();
    }
    public void createAim(ActionEvent actionEvent) {
        Matcher matcher = MainController.pattern.matcher(aimName.getText());
        if(matcher.matches() && aimName.getText().toString().length() < 33 && aimName.getText().toString().trim().length() > 0 ) {
            toServer.getConnector().actionEventSolver.addAim(projectName, aimName.getText(), Integer.parseInt(aimPriorityBox.getValue().toString()));
            CreateAimStage.close();
        }
    }
}
