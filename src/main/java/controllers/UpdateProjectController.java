package controllers;

import clientside.ThreadToWrite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static server_interaction.Commands.UProject;
import static controllers.MainController.choosedIdOfProject;
import static main.Main.connector;

public class UpdateProjectController {
    static public TableView prTable;
    static public TableView aiTable;
    @FXML
    public static Stage UpdateProjectStage;
    @FXML
    public TextField newProjectName;

    public void updateProject(ActionEvent actionEvent) {
        //Main.projects.create(new Project(projectName.getText()));
        Thread t0 = new ThreadToWrite(connector, UProject(choosedIdOfProject, newProjectName.getText()));
        t0.start();
        UpdateProjectStage.close();
        MainController.refresh(prTable, aiTable, -1);
    }
}
