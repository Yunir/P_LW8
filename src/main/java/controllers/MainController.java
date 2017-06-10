package controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import objects.TableviewObservableLists.AimsHolder;
import objects.TableviewObservableLists.ProjectsHolder;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import general_classes.Main;
import objects.Aim;
import objects.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Lang;
import utils.LocaleManager;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static general_classes.Main.cc;
import static general_classes.Main.toServer;

public class MainController extends Observable implements Initializable {
    public static volatile boolean confirmationReceived = false;
    public static AimsHolder aimsHolder;
    public static ProjectsHolder projectsHolder;
    public static Class c = Aim.class;
    private static final String RU_CODE = "ru";
    private static final String EN_CODE = "en_ca";
    private static final String HR_CODE = "hr_ru";
    private static final String BE_CODE = "be_by";
    public static Pattern pattern = Pattern.compile(
            "[" +
                    "a-zA-Zа-яА-ЯёЁ" +
                    "\\s" +         //знаки-разделители (пробел, табуляция и т.д.)
                    "]" +
                    "*");


    @FXML
    private volatile TableView<Project> projectsTable;
    @FXML
    private TableColumn<Project, String> nameOfProject;
    @FXML
    private TableColumn<Project, Integer> amountOfAims;
    @FXML
    private volatile TableView<Aim> aimsTable;
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
    private ComboBox comboLocales;
    private ResourceBundle resourceBundle;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        nameOfProject.setCellValueFactory(new PropertyValueFactory<Project, String>("name"));
        amountOfAims.setCellValueFactory(new PropertyValueFactory<Project, Integer>("amount"));

        nameOfAim.setCellValueFactory(new PropertyValueFactory<Aim, String>("name"));
        priorityOfAim.setCellValueFactory(new PropertyValueFactory<Aim, Integer>("priority"));
        aimsTable.setPlaceholder(new Label(resourceBundle.getString("main.no_chosen_project")));
        ACreate.setDisable(true);
        AUpdate.setDisable(true);
        ADelete.setDisable(true);
        PUpdate.setDisable(true);
        PDelete.setDisable(true);
        initListeners();
        fillLangComboBox();
        fillData();
        aimsHolder = new AimsHolder();
        projectsHolder = new ProjectsHolder();
    }

    public void putDataToObservableList () {
        System.out.println("putting Data to ObservableLists");
            projectsHolder.setProjectsObsList(FXCollections.observableArrayList(Main.dataHolder.getProjects()));
            projectsTable.setItems(projectsHolder.getProjectsObsList());

        //projectsHolder.showAllProjects();
    }

    private void fillData() {
        System.out.println("putting Data to ObservableLists");
        if(projectsHolder != null) {
            projectsHolder.setProjectsObsList(FXCollections.observableArrayList(Main.dataHolder.getProjects()));
            projectsTable.setItems(projectsHolder.getProjectsObsList());
        } else {
            projectsHolder = new ProjectsHolder();
            projectsHolder.setProjectsObsList(FXCollections.observableArrayList());
            projectsTable.setItems(projectsHolder.getProjectsObsList());
        }
    }

    public void openAimsOfProject(MouseEvent mouseEvent) {
        Project selectedProject = (Project) ((TableView)mouseEvent.getSource()).getSelectionModel().getSelectedItem();
        if(selectedProject == null)return;
        c = Project.class;
        aimsHolder.setAimsObsList(FXCollections.observableArrayList(selectedProject.getAimsList()));
        aimsTable.setItems(aimsHolder.getAimsObsList());
        ACreate.setDisable(false);
        PUpdate.setDisable(false);
        PDelete.setDisable(false);
    }

    public void showCreateProjectDialog(ActionEvent actionEvent) {
        Stage stage = cc.showCreateProjectDialog(actionEvent, resourceBundle.getString("create_project_title"));
        stage.show();
        CreateProjectController.CreateProjectStage = stage;
        CreateProjectController.prTable = projectsTable;
        UpdateProjectController.aiTable = aimsTable;
    }
    public void showUpdateProjectDialog(ActionEvent actionEvent) {
        Stage stage = cc.showUpdateProjectDialog(actionEvent, resourceBundle.getString("update_project_title"));
        stage.show();
        UpdateProjectController.UpdateProjectStage = stage;
        UpdateProjectController.prTable = projectsTable;
        UpdateProjectController.aiTable = aimsTable;
    }
    public void deleteProject(ActionEvent actionEvent) {
        toServer.getConnector().actionEventSolver.deleteProject(projectsTable.getSelectionModel().getSelectedItem().getName());
    }

    public void deleteAim(ActionEvent actionEvent) {
        toServer.getConnector().actionEventSolver.deleteAim(projectsTable.getSelectionModel().getSelectedItem().getName(), aimsTable.getSelectionModel().getSelectedItem().getName());
    }

    public void createObjTable(ActionEvent actionEvent) {
        toServer.getConnector().actionEventSolver.createObj();
    }

    public void showCreateAimDialog(ActionEvent actionEvent) {
        Stage stage = cc.showCreateAimDialog(actionEvent, resourceBundle.getString("create_aim_title"));
        CreateAimController.CreateAimStage = stage;
        CreateAimController.projectName = projectsTable.getSelectionModel().getSelectedItem().getName();
        stage.show();
    }

    public void unlockButtons(MouseEvent mouseEvent) {
        Aim temp = (Aim) ((TableView)mouseEvent.getSource()).getSelectionModel().getSelectedItem();
        if(temp==null)return;
        c = Aim.class;
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
    public TableView getAimsTable() {
        return aimsTable;
    }

    private void fillLangComboBox() {
        Lang langRU = new Lang(0, RU_CODE, resourceBundle.getString("ru"), LocaleManager.RU_LOCALE);
        Lang langEN = new Lang(1, EN_CODE, resourceBundle.getString("en"), LocaleManager.EN_LOCALE);
        Lang langBE = new Lang(2, BE_CODE, resourceBundle.getString("be"), LocaleManager.BE_LOCALE);
        Lang langHR = new Lang(3, HR_CODE, resourceBundle.getString("hr"), LocaleManager.HR_LOCALE);

        comboLocales.getItems().add(langRU);
        comboLocales.getItems().add(langEN);
        comboLocales.getItems().add(langBE);
        comboLocales.getItems().add(langHR);

        if(LocaleManager.getCurrentLang()==null){
            comboLocales.getSelectionModel().select(0);
            Lang selectedLang = (Lang) comboLocales.getSelectionModel().getSelectedItem();
            LocaleManager.setCurrentLang(selectedLang);
        } else {
            comboLocales.getSelectionModel().select(LocaleManager.getCurrentLang().getIndex());
        }
    }

    private void initListeners() {
        comboLocales.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Lang selectedLang = (Lang) comboLocales.getSelectionModel().getSelectedItem();
                LocaleManager.setCurrentLang(selectedLang);

                setChanged();
                notifyObservers(selectedLang);
            }
        });
    }

    public void showUpdateAimDialog(ActionEvent actionEvent) {
        Stage stage = cc.showUpdateAimDialog(actionEvent, resourceBundle.getString("update_aim_title"));
        UpdateAimController.UpdateAimStage = stage;
        UpdateAimController.projectName = projectsTable.getSelectionModel().getSelectedItem().getName();
        UpdateAimController.oldAimName = aimsTable.getSelectionModel().getSelectedItem().getName();
        stage.show();

    }
}
