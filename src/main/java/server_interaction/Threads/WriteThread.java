package server_interaction.Threads;

import static main.Main.connector;

public class WriteThread extends Thread {

    String sms;

    @Override
    public void run(){
        connector.ioFuncs.writeToServer(sms);
    }

    public WriteThread(String sms){
        this.sms = sms;
    }
}