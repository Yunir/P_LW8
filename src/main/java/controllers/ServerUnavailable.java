package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class ServerUnavailable {
    public void exitTheApp(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
