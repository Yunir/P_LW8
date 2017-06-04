package server_interaction.Threads;

import server_interaction.IOFuncs;

import java.net.SocketException;

/**
 * Created by Yunicoed on 31.05.2017.
 */
public class AwaitChangesThread extends Thread {
    //Thread loginAwaitThread;
    IOFuncs ioFuncs;
    public AwaitChangesThread(IOFuncs ioFuncs) {
        this.ioFuncs = ioFuncs;
    //    this.loginAwaitThread = loginAwaitThread;
    }

    @Override
    public void run() {
            ioFuncs.awaitOfUpdates();
    }
}
