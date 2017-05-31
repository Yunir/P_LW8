package clientside;

import static main.Main.connector;
import static main.Main.mainController;

/**
 * Created by Yunicoed on 17.05.2017.
 */
public class ThreadToRead extends Thread {
    volatile public boolean await_of_collection = false;
    Thread loginAwaitThread;
    public ThreadToRead(Thread loginAwaitThread) {
        this.loginAwaitThread = loginAwaitThread;
    }

    @Override
    public void run(){
        try {
            loginAwaitThread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }
        connector.listenToServer();
    }
}
