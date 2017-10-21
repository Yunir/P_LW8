package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;

import static general_classes.Main.toServer;

public class CreateNoteController {
    @FXML
    public static Stage CreateNoteStage;
    @FXML
    public TextField text;
    @FXML
    public ComboBox importance;
    @FXML
    public void initialize() {
        for (int i = 1; i < 26; i++) {
            importance.getItems().add(i);
        }
        importance.getSelectionModel().selectFirst();
    }
    public void createNote(ActionEvent actionEvent) {
        Matcher matcher = MainController.pattern.matcher(text.getText());
        if(matcher.matches() && text.getText().toString().length() < 33 && text.getText().toString().trim().length() > 0 ) {
            toServer.getConnector().actionEventSolver.addNote(text.getText(), Integer.parseInt(importance.getValue().toString()));
            CreateNoteStage.close();
        }
    }
}
