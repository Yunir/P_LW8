package server_interaction;

import java.io.*;

/**
 * Created by Yunicoed on 31.05.2017.
 */
public class IOFuncs {
    private DataInputStream dIn;
    private DataOutputStream dOut;

    public IOFuncs(DataInputStream dIn, DataOutputStream dOut) {
        this.dIn = dIn;
        this.dOut = dOut;
    }

    synchronized public void writeToServer(String sms){
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader keyboard = new BufferedReader(isr);

        String line = null;
        try {
            /*if(con_established) {

            }*/

            line = sms;
            //System.out.println(line);
            dOut.writeUTF(line);
            System.out.println(":");
            dOut.flush();
        } catch (IOException e) {
            System.out.println("Something incorrect in writeToServer");
        }
    }
}
