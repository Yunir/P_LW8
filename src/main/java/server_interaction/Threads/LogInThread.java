package server_interaction.Threads;

import java.io.IOException;
import static main.Main.connector;
import static main.Main.mainController;

/**
 * Created by Yunicoed on 31.05.2017.
 */
public class LogInThread extends Thread {
    @Override
    public void run() {
        try {
            boolean logpassCorrect = false;
            while(!logpassCorrect) {
                if (connector.ioFuncs.getdIn().readUTF().equals("allow")) {
                    logpassCorrect = true;
                    mainController.hideLogInDialog();
                } else System.out.println("login or password are incorrect"); //TODO: 3 atempts, then disconnect
            }



        } catch (IOException e) {e.printStackTrace();}

    }
}
