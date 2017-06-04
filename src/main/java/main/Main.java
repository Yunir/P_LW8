package main;

import client_interaction.IOServer;
import client_interaction.PacketOfData;
import client_interaction.ReceiveChangesServerThread;
import data_processing.Database;
import objects.DataHolder;

public class Main {
    public static DataHolder dataHolder;
    public static volatile PacketOfData generalPacketOfData = new PacketOfData();
    public static volatile PacketOfData temp = new PacketOfData();
    public static volatile  boolean notifyEveryone = false;
    public static Database DB;
    public static void main(String[] args) {
        DB = new Database();
        dataHolder = new DataHolder();
        DB.getFullDB();
        ReceiveChangesServerThread receiveChangesServerThread = new ReceiveChangesServerThread();
        receiveChangesServerThread.start();
        IOServer IOServer = new IOServer();
        IOServer.waitConnections();
        System.exit(0);
    }
}
