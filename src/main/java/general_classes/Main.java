package general_classes;

import client_interaction.PacketOfData;
import data_processing.Database;
import objects.DataHolder;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static DataHolder dataHolder;
    public static volatile PacketOfData generalPacketOfData = new PacketOfData();
    public static volatile boolean notifyEveryone = false;
    public static Database DB;

    public static volatile ReentrantLock locker = new ReentrantLock();
    public static volatile Condition updates = locker.newCondition();

    public static void main(String[] args) {
        dataHolder = new DataHolder();
        DB = new Database();
        DB.getFullDB();
        ToServer toServer = new ToServer();
        FromServer fromServer = new FromServer();
        toServer.start();
        fromServer.start();
    }
}
