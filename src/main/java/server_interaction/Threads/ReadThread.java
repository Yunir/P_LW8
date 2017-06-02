package server_interaction.Threads;

import static main.Main.IOConnector;

public class ReadThread extends Thread {
    volatile public boolean await_of_collection = false;
    private Thread loginAwaitThread;
    public ReadThread(Thread loginAwaitThread) {
        this.loginAwaitThread = loginAwaitThread;
    }

    @Override
    public void run(){
        try {
            loginAwaitThread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }
        //IOConnector.ioFuncs.readFromServer();
    }
}
