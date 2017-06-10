package general_classes;

import controllers.ConnectController;
import controllers.MainController;
import controllers.ServerUnavailableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.Lang;
import utils.LocaleManager;

import java.io.IOException;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class ControllerCreator implements Observer {
    FXMLLoader fxmlLoader = null;
    private VBox currentRoot;
    private static final String FXML_MAIN = "../fxml/main.fxml";
    public static final String BUNDLES_FOLDER = "bundles.Locale";
    private Stage primaryStage;
    private MainController mainController;

    /*Create Controllers*/
    MainController showMainView(Stage parent) {
        this.primaryStage = parent;
        createGUI(LocaleManager.RU_LOCALE);
        return fxmlLoader.getController();
    }
    ConnectController showLogInDialog(Stage parent) {
        Stage logInStage = new Stage(StageStyle.TRANSPARENT);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/connect.fxml"));
            loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.RU_LOCALE));
            Parent root = loader.load();
            logInStage.setResizable(false);
            logInStage.setScene(new Scene(root));
            logInStage.initModality(Modality.WINDOW_MODAL);
            logInStage.initOwner(parent);
            logInStage.show();
            ConnectController.stage = logInStage;
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    ServerUnavailableController prepareServerUnavailableDialog(Stage parent) {
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/serverUnavailable.fxml"));
            Parent root = loader.load();
            stage.setMinHeight(150);
            stage.setMinWidth(300);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parent);
            ServerUnavailableController.stage = stage;
            return loader.getController();
        } catch (IOException e) {
            System.out.println("Can'readThread load fxml 'connect' file: " + e.getMessage());
            return null;
        }
    }

    public Stage showUpdateProjectDialog(ActionEvent actionEvent, String update_project_title) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/updateProject.fxml"));
            loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.getCurrentLang().getLocale()));
            Parent root = loader.load();
            stage.setTitle(update_project_title);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    @Override
    public void update(Observable o, Object arg) {
        Lang lang = (Lang) arg;
        VBox newNode = loadFXML(lang.getLocale()); // получить новое дерево компонетов с нужной локалью
        currentRoot.getChildren().setAll(newNode.getChildren());
    }

    private VBox loadFXML(Locale locale) {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(FXML_MAIN));
        fxmlLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
        VBox node = null;
        try {
            node = (VBox) fxmlLoader.load();

            mainController = fxmlLoader.getController();
            mainController.addObserver(this);
            primaryStage.setTitle(fxmlLoader.getResources().getString("main.title"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return node;
    }

    private void createGUI(Locale locale) {
        currentRoot = loadFXML(locale);
        Scene scene = new Scene(currentRoot, 650, 650);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    public Stage showCreateProjectDialog(ActionEvent actionEvent, String create_project_title) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/createProject.fxml"));
            loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.getCurrentLang().getLocale()));
            Parent root = loader.load();
            stage.setTitle(create_project_title);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    public Stage showCreateAimDialog(ActionEvent actionEvent, String create_aim_title) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/createAim.fxml"));
            loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.getCurrentLang().getLocale()));
            Parent root = loader.load();

            stage.setTitle(create_aim_title);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    public Stage showUpdateAimDialog(ActionEvent actionEvent, String create_project_title) {
        Stage stage =new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/updateAim.fxml"));
            loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.getCurrentLang().getLocale()));
            Parent root = loader.load();
            stage.setTitle(create_project_title);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }
}
