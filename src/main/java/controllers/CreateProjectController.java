package controllers;

import clientside.ThreadToWrite;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static clientside.SampleCommands.CProject;
import static main.Main.connector;

public class CreateProjectController {
    static public TableView prTable;
    static public TableView aiTable;
    @FXML
    public static Stage CreateProjectStage;
    @FXML
    public TextField projectName;

    public void createProject(ActionEvent actionEvent) {
        //Main.projects.create(new Project(projectName.getText()));
        Thread t0 = new ThreadToWrite(connector, CProject(projectName.getText()));
        t0.start();
        CreateProjectStage.close();
        MainController.refresh(prTable, aiTable, -1);
    }
}
