package controllers;

import server_interaction.Threads.WriteThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static server_interaction.Commands.CAim;
import static controllers.CreateProjectController.aiTable;

public class CreateAimController {
    @FXML
    public static Stage CreateAimStage;
    @FXML
    public TextField aimName;
    @FXML
    public TextField aimPriority;

    public void createAim(ActionEvent actionEvent) {
        //Main.projects.create(new Project(projectName.getText()));
        //TODO check it is 32 symbols and have only characters
        //TODO sort - when add
        Thread t = new WriteThread(CAim(MainController.choosedIdOfProject, aimName.getText(), Integer.parseInt(aimPriority.getText())));
        t.start();
        CreateAimStage.close();
        //MainController.refreshAimTable(aiTable, MainController.choosedIdOfProject);

    }
}
