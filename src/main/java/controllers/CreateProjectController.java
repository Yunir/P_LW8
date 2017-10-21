package controllers;

import javafx.application.Platform;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;

import static general_classes.Main.mainController;
import static general_classes.Main.toServer;
import static java.lang.Thread.sleep;

public class CreateProjectController {
    @FXML
    static public TableView prTable;
    static public TableView aiTable;
    @FXML
    public static Stage CreateProjectStage;
    @FXML
    public TextField projectName;
    public static String projects = "";

    public void createProject(ActionEvent actionEvent) {
        Matcher matcher = MainController.pattern.matcher(projectName.getText());
        if (matcher.matches() && projectName.getText().toString().length() < 33 && projectName.getText().toString().trim().length() > 0) {
            if (projects.equals("")) projects += projectName.getText();
            else projects = projects + ";" + projectName.getText();
            toServer.getConnector().actionEventSolver.addProject(projects);
            projects = "";
            CreateProjectStage.close();
            /*Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Update in close");
                    mainController.putDataToObservableList();
                    prTable.refresh();
                }
            });*/
        }
    }

    public void nextProject(ActionEvent actionEvent) {
        Matcher matcher = MainController.pattern.matcher(projectName.getText());
        if(matcher.matches() && projectName.getText().toString().length() < 33 && projectName.getText().toString().trim().length() > 0) {
            if (projects.equals("")) projects += projectName.getText();
            else projects = projects + ";" + projectName.getText();
            projectName.setText("");
        }
    }
}
