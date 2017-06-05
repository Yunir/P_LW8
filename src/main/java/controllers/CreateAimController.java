package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    public void createAim(ActionEvent actionEvent) {
        //TODO check it is 32 symbols and have only characters
        toServer.getConnector().actionEventSolver.addAim(projectName, aimName.getText(), Integer.parseInt(aimPriority.getText()));
        CreateAimStage.close();

    }
}
