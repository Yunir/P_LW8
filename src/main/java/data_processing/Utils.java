package data_processing;

import objects.Aim;
import objects.Project;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Utils {


    public static ByteArrayOutputStream ObjectToString(Object o){
        ByteArrayOutputStream bytearr = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bytearr);
            oos.writeObject(o);
            /*try {
                oos.flush();
                bytearr.flush();
                oos.close();
                bytearr.close();
                ByteArrayInputStream ibytearr = new ByteArrayInputStream(bytearr.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(ibytearr);
                SendPacket s = (SendPacket)ois.readObject();
                System.out.println(s.name + " " + s.additional);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytearr;
    }
}
