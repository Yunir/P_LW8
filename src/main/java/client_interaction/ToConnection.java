package client_interaction;

import general_classes.MessageSolver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ToConnection extends Thread {
    private final String USER_ACCEPTED = "The user %d is accepted\n\n";
    private MessageSolver messageSolver;
    private int idOfConnection;
    private Socket socket = null;

    public ToConnection(int idOfConnection, Socket socket) {
        try {
            messageSolver = new MessageSolver(new DataInputStream (socket.getInputStream()), new DataOutputStream(socket.getOutputStream()), idOfConnection);
            this.idOfConnection = idOfConnection;
            this.socket = socket;
            setDaemon(true);
            setPriority(NORM_PRIORITY);
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.printf(USER_ACCEPTED, idOfConnection);
        try {
            messageSolver.checkAuthData();
            messageSolver.startConversationWithUser();
        } catch (IOException e) {
            try {
                System.out.println("User " + idOfConnection + " stopped the connection.");
                socket.close();
            } catch (IOException ee) {ee.printStackTrace();}
        }

    }
}
