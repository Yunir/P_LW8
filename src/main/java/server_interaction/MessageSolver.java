package server_interaction;

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

    public MessageSolver() {
        gson = new GsonBuilder().create();

    }

    public String serializePacketOfData (PacketOfData packetOfData) {
        return gson.toJson(packetOfData);
    }
    public PacketOfData deserializePacketOfData (String message) {
        return gson.fromJson(message, PacketOfData.class);
    }
}
