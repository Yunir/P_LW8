package controllers;

import objects.TableviewObservableLists.AimsHolder;
import objects.TableviewObservableLists.ProjectsHolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import general_classes.Main;
import objects.Aim;
import objects.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static general_classes.Main.toServer;

public class MainController {
    public static volatile boolean confirmationReceived = false;
    public static AimsHolder aimsHolder;
    public static ProjectsHolder projectsHolder;

    @FXML
    private TableView<Project> projectsTable;
    @FXML
    private TableColumn<Project, String> nameOfProject;
    @FXML
    private TableColumn<Project, Integer> amountOfAims;
    @FXML
    private TableView aimsTable;
    @FXML
    private TableColumn<Aim, String> nameOfAim;
    @FXML
    private TableColumn<Aim, Integer> priorityOfAim;
    @FXML
    private Button PUpdate;
    @FXML
    private Button PDelete;
    @FXML
    private Button ACreate;
    @FXML
    private Button AUpdate;
    @FXML
    private Button ADelete;
    @FXML
    private void initialize() {
        nameOfProject.setCellValueFactory(new PropertyValueFactory<Project, String>("name"));
        amountOfAims.setCellValueFactory(new PropertyValueFactory<Project, Integer>("amount"));

        nameOfAim.setCellValueFactory(new PropertyValueFactory<Aim, String>("name"));
        priorityOfAim.setCellValueFactory(new PropertyValueFactory<Aim, Integer>("priority"));
        aimsTable.setPlaceholder(new Label("Click to any project to start working with aims."));
        ACreate.setDisable(true);
        AUpdate.setDisable(true);
        ADelete.setDisable(true);
        PUpdate.setDisable(true);
        PDelete.setDisable(true);

        aimsHolder = new AimsHolder();
        projectsHolder = new ProjectsHolder();
    }

    public void putDataToObservableList () {
        System.out.println("putting Data to ObservableLists");
        projectsHolder.setProjectsObsList(FXCollections.observableArrayList(Main.data.getProjects()));
        projectsTable.setItems(projectsHolder.getProjectsObsList());

        projectsHolder.showAllProjects();
    }

    public void openAimsOfProject(MouseEvent mouseEvent) {
        Project selectedProject = (Project) ((TableView)mouseEvent.getSource()).getSelectionModel().getSelectedItem();
        if(selectedProject == null)return;
        aimsHolder.setAimsObsList(FXCollections.observableArrayList(selectedProject.getAimsList()));
        aimsTable.setItems(aimsHolder.getAimsObsList());
        ACreate.setDisable(false);
        PUpdate.setDisable(false);
        PDelete.setDisable(false);
    }

    public void showCreateProjectDialog(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/createProject.fxml"));
            stage.setTitle("New project");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            CreateProjectController.CreateProjectStage = stage;
            CreateProjectController.prTable = projectsTable;
            UpdateProjectController.aiTable = aimsTable;
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showUpdateProjectDialog(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            UpdateProjectController.oldProjectName = projectsTable.getSelectionModel().getSelectedItem().getName();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/updateProject.fxml"));
            stage.setTitle("Change name of project");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            UpdateProjectController.UpdateProjectStage = stage;
            UpdateProjectController.prTable = projectsTable;
            UpdateProjectController.aiTable = aimsTable;
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteProject(ActionEvent actionEvent) {
        toServer.getConnector().actionEventSolver.deleteProject(projectsTable.getSelectionModel().getSelectedItem().getName());
    }

    public void showCreateAimDialog(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../../resources/fxml/createAim.fxml"));
            stage.setTitle("New aim");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            CreateAimController.CreateAimStage = stage;
            CreateProjectController.aiTable = aimsTable;
            stage.show();
        } catch (IOException e) {
            System.out.println("Can'readThread load fxml 'createAim' file");
        }
    }

    public void unlockButtons(MouseEvent mouseEvent) {
        Aim temp = (Aim) ((TableView)mouseEvent.getSource()).getSelectionModel().getSelectedItem();
        if(temp==null)return;
        AUpdate.setDisable(false);
        ADelete.setDisable(false);
    }
    public void disableUpdateDeleteButtons() {
        PUpdate.setDisable(true);
        PDelete.setDisable(true);
        ACreate.setDisable(true);
        AUpdate.setDisable(true);
        ADelete.setDisable(true);
    }
    public TableView getProjectsTable() {
        return projectsTable;
    }
}
