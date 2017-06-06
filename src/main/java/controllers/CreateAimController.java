package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static general_classes.Main.toServer;

public class CreateAimController {
    public static String projectName;
    @FXML
    public static Stage CreateAimStage;
    @FXML
    public TextField aimName;
    @FXML
    public TextField aimPriority;
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
        //TODO check it is 32 symbols and have only characters
        toServer.getConnector().actionEventSolver.addAim(projectName, aimName.getText(), Integer.parseInt(aimPriorityBox.getValue().toString()));
        CreateAimStage.close();

    }
}
