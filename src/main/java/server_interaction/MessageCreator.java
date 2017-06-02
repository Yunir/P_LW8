package server_interaction;


/**
 * Created by Yunicoed on 02.06.2017.
 */
public class MessageCreator {
    PacketOfData packetOfData;
    public PacketOfData firstRead() {
        packetOfData = new PacketOfData();
        packetOfData.setCommandType(packetOfData.get_FIRST_READ());
        return packetOfData;
    }
}
