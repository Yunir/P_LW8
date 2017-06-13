package controllers;

import general_classes.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Notes;
import objects.Project;
import objects.TableviewObservableLists.NotesHolder;
import objects.TableviewObservableLists.ProjectsHolder;

import static controllers.UpdateNoteController.oldNoteName;
import static general_classes.Main.cc;
import static general_classes.Main.toServer;

public class ReflTableController {
    public static Stage reflTableStage;
    public static NotesHolder notesHolder = new NotesHolder();
    @FXML
    private volatile TableView<Notes> notes;
    @FXML
    private TableColumn<Notes, String> text;
    @FXML
    private TableColumn<Notes, Integer> importance;
    @FXML
    private Button create;
    @FXML
    private Button update;
    @FXML
    private Button delete;
    @FXML
    public void initialize() {
        text.setCellValueFactory(new PropertyValueFactory<Notes, String>("text"));
        importance.setCellValueFactory(new PropertyValueFactory<Notes, Integer>("importance"));

        //notes.setPlaceholder(new Label("There is no notes, yet..."));
        update.setDisable(true);
        delete.setDisable(true);
        fillData();
    }

    private void fillData() {
        System.out.println("putting Data to ObservableLists");
        notesHolder.setNotesObsList(FXCollections.observableArrayList(Main.dataHolder.getNotesList()));
        notes.setItems(notesHolder.getNotesObsList());
    }

    public void showCreateNote(ActionEvent actionEvent) {
        Stage stage = cc.showCreateNoteDialog(actionEvent, "Создай заметку!");
        stage.show();
        CreateNoteController.CreateNoteStage = stage;
    }

    public void showUpdateNote(ActionEvent actionEvent) {
        Stage stage = cc.showUpdateNoteDialog(actionEvent, "Создай заметку!");
        stage.show();
        UpdateNoteController.UpdateNoteStage = stage;
        update.setDisable(true);
        delete.setDisable(true);
    }

    public void deleteNote(ActionEvent actionEvent) {
        toServer.getConnector().actionEventSolver.deleteNote(notes.getSelectionModel().getSelectedItem().getText());
        notes.refresh();
        notes.setItems(ReflTableController.notesHolder.getNotesObsList());
        update.setDisable(true);
        delete.setDisable(true);
        reflTableStage.close();
    }

    public void selectNote(MouseEvent mouseEvent) {
        Notes selectedItem = (Notes) ((TableView)mouseEvent.getSource()).getSelectionModel().getSelectedItem();
        if(selectedItem == null)return;
        oldNoteName = selectedItem.getText();
        notes.refresh();
        update.setDisable(false);
        delete.setDisable(false);
    }
}
