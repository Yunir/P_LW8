package controllers;

import objects.TableviewObservableLists.AimsHolder;
import objects.TableviewObservableLists.ProjectsHolder;
import server_interaction.MessageSolver;
import server_interaction.PacketOfData;
import server_interaction.Threads.WriteThread;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import main.Main;
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
import java.util.Date;

import static server_interaction.Commands.RAim;
import static server_interaction.Commands.RProject;

public class MainController {
    private Stage logInStage;
    public static AimsHolder aimsHolder;
    public static ProjectsHolder projectsHolder;
    static int choosedIdOfProject = -1;
    static private boolean disconnected = false;
    @FXML
    private TableView projectsTable;
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

    }

    public void initializeDataObsLists() {
        aimsHolder = new AimsHolder();
        projectsHolder = new ProjectsHolder();
    }

    //Control pop-up logIn Stage
    public void showLogInDialog(Stage parent) {
        logInStage = new Stage(StageStyle.TRANSPARENT);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/connect.fxml"));
            logInStage.setTitle("Logging in");
            logInStage.setMinHeight(150);
            logInStage.setMinWidth(300);
            logInStage.setResizable(false);
            logInStage.setScene(new Scene(root));
            logInStage.initModality(Modality.WINDOW_MODAL);
            logInStage.initOwner(parent);
            ConnectController.LoginStage = logInStage;
            logInStage.show();
        } catch (IOException e) {
            System.out.println("Can'readThread load fxml 'connect' file: " + e.getMessage());
        }
    }
    public void hideLogInDialog() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                logInStage.close();
            }
        });
    }

    public void showCreateProjectDialog(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../../resources/fxml/createProject.fxml"));
            stage.setTitle("New project");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            CreateProjectController.CreateProjectStage = stage;
            CreateProjectController.prTable = projectsTable;
            stage.show();
        } catch (IOException e) {
            System.out.println("Can'readThread load fxml 'createProject' file");
        }
    }

    public void showUpdateProjectDialog(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../../resources/fxml/updateProject.fxml"));
            stage.setTitle("Change name of project");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            UpdateProjectController.UpdateProjectStage = stage;
            UpdateProjectController.prTable = projectsTable;
            stage.show();
        } catch (IOException e) {
            System.out.println("Can't readThread load fxml 'createProject' file");
        }
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

    public void openAimsOfProject(MouseEvent mouseEvent) {
        Project selectedProject = (Project) ((TableView)mouseEvent.getSource()).getSelectionModel().getSelectedItem();
        if(selectedProject==null)return;
        choosedIdOfProject = selectedProject.getId();
        System.out.println(selectedProject.getId() + " " + selectedProject.getAmount() + " " + selectedProject.getName());
        refresh(projectsTable, aimsTable, selectedProject.getId());
        aimsTable.setPlaceholder(new Label("There is no aims yet."));
        ACreate.setDisable(false);
        PUpdate.setDisable(false);
        PDelete.setDisable(false);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                projectsTable.requestFocus();
                projectsTable.getSelectionModel().select(selectedProject.getId()-1);
                projectsTable.getFocusModel().focus(selectedProject.getId()-1);
            }
        });
        //aimsTable.setItems(temp.aims.getAims());
    }

    public void refreshScene(ActionEvent actionEvent) {
        choosedIdOfProject = -1;
        refresh(projectsTable, aimsTable, -1);
        //TODO: flush Aimlist
        ACreate.setDisable(true);
        AUpdate.setDisable(true);
        ADelete.setDisable(true);
        PUpdate.setDisable(true);
        PDelete.setDisable(true);
        aimsTable.setItems(FXCollections.observableArrayList());
        aimsTable.setPlaceholder(new Label("Click to any project to start working with aims."));
    }

    static public void refresh(TableView s, TableView ss, int currProjectId) {
        System.out.println("Refresh started...");
        choosedIdOfProject = currProjectId;
        System.out.println(Main.readThread.await_of_collection);
        if(currProjectId == -1) {
            Thread t1 = new WriteThread(RProject());
            Main.readThread.await_of_collection = true;
            t1.start();
        } else {
            Thread t2 = new WriteThread(RAim(currProjectId));
            Main.readThread.await_of_collection = true;
            t2.start();
        }
        System.out.println("Install awaiting value - " + Main.readThread.await_of_collection);

       // if (IOConnector.con_established) {        }
        Date oldDate = new Date();
            Date newDate;
            long seconds;
            System.out.println("Before trip value - " + Main.readThread.await_of_collection);
            while(Main.readThread.await_of_collection) {
                newDate = new Date();
                seconds = (newDate.getTime()-oldDate.getTime())/1000;
                if (seconds > 5) {
                    System.out.println("Response of refresh time over");
                    disconnected = true;
                    Main.readThread.await_of_collection = false;
                }
            }
            s.setItems(projectsHolder.getProjectsObsList());
            if(currProjectId != -1) ss.setItems(aimsHolder.getAimsObsList());
    }

    public void refresh() {
        MessageSolver m = new MessageSolver();
        PacketOfData p = new PacketOfData();
        System.out.println("Refresh started...");
        System.out.println(Main.readThread.await_of_collection);
        Thread t1 = new WriteThread(m.serializePacketOfData(p));
        Main.readThread.await_of_collection = true;
        t1.start();
        /*if(choosedIdOfProject == -1) {
            Thread t1 = new WriteThread(RProject());
            Main.readThread.await_of_collection = true;
            t1.start();
        } else {
            Thread t2 = new WriteThread(RAim(choosedIdOfProject));
            Main.readThread.await_of_collection = true;
            t2.start();
        }
        System.out.println("Install awaiting value - " + Main.readThread.await_of_collection);

        //if (IOConnector.con_established) {}
            Date oldDate = new Date();
            Date newDate;
            long seconds;
            System.out.println("Before trip value - " + Main.readThread.await_of_collection);
            while(Main.readThread.await_of_collection) {
                newDate = new Date();
                seconds = (newDate.getTime()-oldDate.getTime())/1000;
                if (seconds > 5) {
                    System.out.println("Response of refresh time over");
                    disconnected = true;
                    Main.readThread.await_of_collection = false;
                }
            }
            projectsTable.setItems(projectsHolder.getProjectsObsList());
            if(choosedIdOfProject != -1) aimsTable.setItems(aimsHolder.getAimsObsList());

        projectsTable.setItems(projectsHolder.getProjectsObsList());*/
    }


    static public void refreshAimTable(TableView ss, int currProjectId) {
        System.out.println("решил рефрешнуть " + currProjectId);
        Thread t2 = new WriteThread(RAim(currProjectId));
        Main.readThread.await_of_collection = true;
        t2.start();

        //if (IOConnector.con_established) {}

            Date oldDate = new Date();
            Date newDate;
            long seconds;
            while(Main.readThread.await_of_collection) {
                newDate = new Date();
                seconds = (newDate.getTime()-oldDate.getTime())/1000;
                if (seconds > 5) {
                    System.out.println("Response time over");
                    disconnected = true;
                    Main.readThread.await_of_collection = false;
                }
            }
            ss.setItems(aimsHolder.getAimsObsList());

    }

    public void unlockButtons(MouseEvent mouseEvent) {
        Aim temp = (Aim) ((TableView)mouseEvent.getSource()).getSelectionModel().getSelectedItem();
        if(temp==null)return;
        AUpdate.setDisable(false);
        ADelete.setDisable(false);
    }
}
