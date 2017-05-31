package clientside;

import server_interaction.Connector;

public class ThreadToWrite extends Thread {

    String sms;
    Connector connector;

    public ThreadToWrite(Connector connector, String sms){
        this.connector = connector;
        this.sms = sms;
    }

    @Override
    public void run(){
        connector.ioFuncs.writeToServer(sms);
    }
}