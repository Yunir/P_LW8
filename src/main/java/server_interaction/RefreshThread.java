package server_interaction;

import static main.Main.mainController;

/**
 * Created by Yunicoed on 31.05.2017.
 */
public class RefreshThread extends Thread {
    Thread loginAwaitThread;
    public RefreshThread(Thread loginAwaitThread) {
        this.loginAwaitThread = loginAwaitThread;
    }

    @Override
    public void run() {
        try {
            loginAwaitThread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }
        mainController.refresh();
    }
}
