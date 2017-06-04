package data_processing;

import client_interaction.PacketOfData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageSolver {
    String query;
    Gson gson;
    String SQLCommand;
    Connection dbConnection;
    //TODO: data_processing of logins and password-hashes
    String name = "admin";
    String Password = DigestUtils.md5Hex("admin");

    public MessageSolver(Connection dbConnection) {
        this.dbConnection = dbConnection;
        gson = new GsonBuilder().create();

    }

    public boolean checkLogPass (String msg) {
        String[] splitedLine = msg.split(";");
        if (name.equals(splitedLine[1]) && Password.equals(splitedLine[2])) return true;
        else return false;
    }

    public String serializePacketOfData (PacketOfData packetOfData) {
        return gson.toJson(packetOfData);
    }

    public void addProject(String name) {
        try {
            activateQuery("INSERT INTO projectholder (project) VALUES(\'" + name + "\');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PacketOfData deserializePacketOfData (String message) {
        return gson.fromJson(message, PacketOfData.class);
    }

    public boolean parseExecuteResult(String line) throws SQLException {
        gson = new GsonBuilder().create();
        PacketOfData packetOfData = new PacketOfData();
        SQLCommand = null;
        String[] splitedLine = line.split(";");
        if (splitedLine[0].equals("project")) {
            switch (splitedLine[1]) {
                case "create":
                    activateQuery("INSERT INTO projectholder (project) VALUES(\'" + splitedLine[2] + "\');");
                case "read":
                    //TODO: nothing to send like String SQL Command
                    //return generalPacketOfData.setProjectsList(Utils.getAllProjects(connection));
                case "update":
                    activateQuery("UPDATE projectholder SET project = \'" + splitedLine[3] + "\' WHERE pkey = "+splitedLine[2]+";");
                case "drop":
                    activateQuery("DELETE from projectholder WHERE pkey = " + splitedLine[2] + ";");
            }
            return true;
        } else if(splitedLine[0].equals("access")){
            String name = "admin";
            String Password = DigestUtils.md5Hex("admin");
            if (name.equals(splitedLine[1]) && Password.equals(splitedLine[2])) return true;
            else return false;
        } /*else {
            switch (splitedLine[1]) {
                case "create":
                    try {
                        activateQuery("INSERT INTO aimholder (pkey, aim, priority) VALUES("+ splitedLine[2]+", \'"+splitedLine[3]+"\', "+splitedLine[4]+");");
                        System.out.println("Insert successfully finished!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        generalPacketOfData.setProjectsList(Utils.getAllProjects(connection));
                        //TODO: tie with numbers
                        //generalPacketOfData.getProjectsList().get(*//*какой-то проект*//*).aimsList = Utils.getAllAims(Integer.parseInt(splitedLine[2]),connection);
                        SQLCommand = gson.toJson(generalPacketOfData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "read":
                    try {
                        generalPacketOfData.setProjectsList(Utils.getAllProjects(connection));
                        //TODO: aimList tie with projects
                        //generalPacketOfData.aimsList = Utils.getAllAims(Integer.parseInt(splitedLine[2]), connection);
                        SQLCommand = gson.toJson(generalPacketOfData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "update":

                    break;
                case "drop":

                    break;
                default:

                    break;
            }
        }*/
        return true;
    }

    public void activateQuery(String q) throws SQLException {
        Statement statement = null;
        String query = q;
        try {
            statement = dbConnection.createStatement();
            statement.execute(query);
            System.out.println("Query successfully completed");
        }  finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
}
