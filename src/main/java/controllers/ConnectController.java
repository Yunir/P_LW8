package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

import static general_classes.Main.toServer;

public class ConnectController {
    @FXML
    public static Stage stage;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Label exceptionOfLogPass;

    public void connectQuery(ActionEvent actionEvent) {
        System.out.println("Send to server log and password");
        try {
            toServer.getConnector().actionEventSolver.write(login.getText()+";"+ DigestUtils.md5Hex(password.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: if only recieve deny
        exceptionOfLogPass.setVisible(true);
    }

    public void exitTheApp(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void hideLogInDialog() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.close();
            }
        });
    }


}
