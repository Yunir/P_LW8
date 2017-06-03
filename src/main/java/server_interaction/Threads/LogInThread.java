package server_interaction.Threads;

import controllers.ConnectController;
import controllers.MainController;
import javafx.application.Platform;

import java.io.IOException;

import static general_classes.Main.*;

/**
 * Created by Yunicoed on 31.05.2017.
 */
public class LogInThread extends Thread {
    @Override
    public void run() {
        try {
            boolean logpassCorrect = false;
            locker.lock();
            System.out.println("lock: logpass");
            while(!logpassCorrect) {
                System.out.println("in loop");
                if (toServer.getConnector().ioFuncs.getdIn().readUTF().equals("allow")) {
                    System.out.println("confirmation received");
                    logpassCorrect = true;
                    MainController.confirmationReceived = true;
                    connectController.hideLogInDialog();
                    condition.signalAll();
                    System.out.println("Всех оповестил");
                } else {
                    System.out.println("login or password are incorrect");
                }
                //TODO: 3 atempts, then disconnect
            }
            locker.unlock();


        } catch (IOException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    serverUnavailableController.showServerUnavailableScene();
                }
            });
        }

    }
}
