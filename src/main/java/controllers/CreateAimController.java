package controllers;

import clientside.ThreadToWrite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static clientside.SampleCommands.CAim;
import static controllers.CreateProjectController.aiTable;
import static main.Main.connector;

public class CreateAimController {
    @FXML
    public static Stage CreateAimStage;
    @FXML
    public TextField aimName;
    @FXML
    public TextField aimPriority;

    public void createAim(ActionEvent actionEvent) {
        //Main.projects.create(new Project(projectName.getText()));
        Thread t5 = new ThreadToWrite(connector, CAim(MainController.choosedIdOfProject, aimName.getText(), Integer.parseInt(aimPriority.getText())));
        t5.start();
        CreateAimStage.close();
        MainController.refreshAimTable(aiTable, MainController.choosedIdOfProject);

    }
}
