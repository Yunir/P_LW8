package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;

import static general_classes.Main.toServer;

/**
 * Created by Yunicoed on 13.06.2017.
 */
public class UpdateNoteController {
    public static String oldNoteName;
    @FXML
    public static Stage UpdateNoteStage;
    @FXML
    public TextField newNoteText;
    @FXML
    public ComboBox importance;
    @FXML
    public void initialize() {
        for (int i = 1; i < 26; i++) {
            importance.getItems().add(i);
        }
        importance.getSelectionModel().selectFirst();
    }

    public void updateNote(ActionEvent actionEvent) {
        Matcher matcher = MainController.pattern.matcher(newNoteText.getText());
        if(matcher.matches() && newNoteText.getText().toString().length() < 33 && newNoteText.getText().toString().trim().length() > 0) {
            toServer.getConnector().actionEventSolver.updateNote(oldNoteName, newNoteText.getText(), Integer.parseInt(importance.getValue().toString()));
            UpdateNoteStage.close();
        }
    }
}
