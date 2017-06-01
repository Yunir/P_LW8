package server_interaction.Threads;

import static main.Main.IOConnector;

public class WriteThread extends Thread {

    String sms;

    @Override
    public void run(){
        IOConnector.ioFuncs.writeToServer(sms);
    }

    public WriteThread(String sms){
        this.sms = sms;
    }
}