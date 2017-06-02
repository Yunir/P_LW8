package server_interaction.Threads;

import server_interaction.IOFuncs;

import static main.Main.mainController;

/**
 * Created by Yunicoed on 31.05.2017.
 */
public class IOThread extends Thread {
    Thread loginAwaitThread;
    public IOThread(Thread loginAwaitThread) {
        this.loginAwaitThread = loginAwaitThread;
    }

    @Override
    public void run() {
        try {
            loginAwaitThread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }
        IOFuncs.getFirstData();
    }
}
