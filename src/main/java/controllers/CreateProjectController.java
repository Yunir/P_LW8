package controllers;

import objects.Project;
import server_interaction.IOFuncs;
import server_interaction.Threads.WriteThread;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static server_interaction.Commands.CProject;

public class CreateProjectController {
    static public TableView prTable;
    static public TableView aiTable;
    @FXML
    public static Stage CreateProjectStage;
    @FXML
    public TextField projectName;

    public void createProject(ActionEvent actionEvent) {
        //Main.projects.create(new Project(projectName.getText()));
        IOFuncs.addProject(projectName.getText());
        CreateProjectStage.close();
        //MainController.getFirstData(prTable, aiTable, -1);
    }
}
