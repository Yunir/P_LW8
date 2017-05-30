package clientside;

import connection.Connector;

/**
 * Created by Yunicoed on 17.05.2017.
 */
public class ThreadToRead extends Thread {
    Connector connector;
    volatile public boolean await_of_collection = false;
    public ThreadToRead(Connector connector){
        this.connector = connector;
    }
    @Override
    public void run(){
        connector.listenToServer();
    }
}
