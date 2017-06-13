package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;

import static general_classes.Main.toServer;

public class UpdateProjectController {
    static public TableView prTable;
    static public TableView aiTable;
    public static String oldProjectName;
    @FXML
    public static Stage UpdateProjectStage;
    @FXML
    public TextField newProjectName;

    public void updateProject(ActionEvent actionEvent) {

        Matcher matcher = MainController.pattern.matcher(newProjectName.getText());
        if(matcher.matches() && newProjectName.getText().toString().length() < 33 && newProjectName.getText().toString().trim().length() > 0) {
        toServer.getConnector().actionEventSolver.updateProject(oldProjectName, newProjectName.getText());
        UpdateProjectStage.close();
    }
}}

