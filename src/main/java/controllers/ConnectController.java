package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import org.apache.commons.codec.digest.DigestUtils;
import server_interaction.MessageSolver;
import server_interaction.PacketOfData;
import server_interaction.Threads.WriteThread;

import static main.Main.IOConnector;

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

        MessageSolver m = new MessageSolver();
        PacketOfData p = new PacketOfData();
        //IOConnector.ioFuncs.writeToServer(m.serializePacketOfData(p));

        IOConnector.ioFuncs.writeToServer("access;"+login.getText()+";"+ DigestUtils.md5Hex(password.getText()));
        exceptionOfLogPass.setVisible(true);
    }

    public void exitTheApp(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
