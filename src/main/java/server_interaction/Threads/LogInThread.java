package server_interaction.Threads;

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
            while(!logpassCorrect) {
                if (toServer.getConnector().ioFuncs.getdIn().readUTF().equals("allow")) {
                    logpassCorrect = true;
                    mainController.hideLogInDialog();
                    condition.signalAll();
                } else {
                    System.out.println("login or password are incorrect");
                }
                //TODO: 3 atempts, then disconnect
            }
            locker.unlock();


        } catch (IOException e) {e.printStackTrace();}

    }
}
