package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import static main.Main.connector;
import static main.Main.start;

public class ConnectController {
    @FXML
    public static Stage LoginStage;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Label exceptionOfLogPass;
    public void connectQuery(ActionEvent actionEvent) {
        connector.writeToServer("access;"+login.getText()+";"+ DigestUtils.md5Hex(password.getText()));
        if(connector.checkLogPassFromServer()) {
            start = true;
            LoginStage.close();
        }
        else exceptionOfLogPass.setVisible(true);
    }

    public void exitTheApp(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
