package controllers;

import javafx.application.Platform;
import server_interaction.Threads.WriteThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static general_classes.Main.toServer;
import static server_interaction.Commands.CAim;
import static controllers.CreateProjectController.aiTable;

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
