/*
package server_interaction.Threads;

import server_interaction.Connector;

import java.io.IOException;

import static general_classes.Main.toServer;

public class WriteThread extends Thread {
    Connector IOConnector;
    String sms;

    public WriteThread(Connector IOConnector, String sms){
        this.IOConnector = IOConnector;
        this.sms = sms;
    }

    @Override
    public void run(){
        IOConnector.ioFuncs.writeToServer(sms);
    }

}*/
