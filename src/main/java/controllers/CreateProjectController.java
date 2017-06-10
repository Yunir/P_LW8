package controllers;

import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;

import static general_classes.Main.toServer;

public class CreateProjectController {
    static public TableView prTable;
    static public TableView aiTable;
    @FXML
    public static Stage CreateProjectStage;
    @FXML
    public TextField projectName;
    public static String projects = "";
    public void createProject(ActionEvent actionEvent) {
        Matcher matcher = MainController.pattern.matcher(projectName.getText());
        if (matcher.matches() && projectName.getText().toString().length() < 33) {
            if (projects.equals("")) projects += projectName.getText();
            else projects = projects + ";" + projectName.getText();
            toServer.getConnector().actionEventSolver.addProject(projects);
            projects = "";
            CreateProjectStage.close();
        }
    }

    public void nextProject(ActionEvent actionEvent) {
        Matcher matcher = MainController.pattern.matcher(projectName.getText());
        if(matcher.matches() && projectName.getText().toString().length() < 33) {
            if (projects.equals("")) projects += projectName.getText();
            else projects = projects + ";" + projectName.getText();
            projectName.setText("");
        }
    }
}
