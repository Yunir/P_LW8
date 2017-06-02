package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import server_interaction.MessageSolver;
import server_interaction.PacketOfData;

import static main.Main.IOConnector;

public class ServerUnavailable {
    public void exitTheApp(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
