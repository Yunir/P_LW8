package general_classes;

import ORM.Attribute;
import ORM.DatabaseManager;
import client_interaction.PacketOfData;
import data_processing.Database;
import objects.Aim;
import objects.DataHolder;
import objects.Notes;
import objects.Project;
import org.w3c.dom.Attr;

import java.time.OffsetDateTime;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static DataHolder dataHolder;
    public static volatile PacketOfData generalPacketOfData = new PacketOfData();
    public static volatile boolean notifyEveryone = false;
    public static Database DB;
    public static DatabaseManager databaseManager;
    public static volatile ReentrantLock locker = new ReentrantLock();
    public static volatile Condition updates = locker.newCondition();

    public static void main(String[] args) {
        databaseManager = new DatabaseManager();
        databaseManager.createTableOfClass(Notes.class, true);
        databaseManager.addObjectToTable(new Notes("today is a great day", 100));
        databaseManager.addObjectToTable(new Notes("c", 100));
        databaseManager.addObjectToTable(new Notes("a", 100));
        databaseManager.addObjectToTable(new Notes("b", 100));
        databaseManager.updateObjectInTable(new Notes("today is a great day", 100), "importance", "9999", "text");
        databaseManager.updateObjectInTable(new Notes("today is a great day", 100), "text", "Today is the best day ever", "text");
        databaseManager.deleteObjectInTable(new Notes("Today is the best day ever", 9999), "text");
        dataHolder = new DataHolder();
        DB = new Database();
        DB.getFullDB();
        ToServer toServer = new ToServer();
        FromServer fromServer = new FromServer();
        toServer.start();
        fromServer.start();
        databaseManager.createTableOfClass(Aim.class, true);
        databaseManager.createTableOfClass(Project.class, true);
        databaseManager.addObjectToTable(new Aim("Pismak", 1, OffsetDateTime.now()));
        }
}
