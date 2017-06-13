package ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private Connection connection;
    private Map<String, TableData> tables = new HashMap<>();

    /**
     * Handle the connection with PostgreSQL Database
     */
    public DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/ProjectsDB", "postgres", "admin");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTableOfClass(Class objectClass, boolean hardReset) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            TableData tableData = new TableData(((Entity) objectClass.getAnnotation(Entity.class)).name());
            if (hardReset) statement.execute("DROP TABLE IF EXISTS " + tableData.getName() + ";");
            Field[] fields = objectClass.getDeclaredFields();
            StringBuilder build = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableData.getName() + " (ID SERIAL PRIMARY KEY");
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Attribute.class)) {
                    tableData.getAttributes().put(field.getAnnotation(Attribute.class).name(), field.getAnnotation(Attribute.class).type());
                    build.append(", " + field.getAnnotation(Attribute.class).name() + " " + field.getAnnotation(Attribute.class).type());
                }
            }
            tables.put(tableData.getName(), tableData);
            build.append(");");
            statement.execute(build.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addObjectToTable(Object object) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            Field[] fields = object.getClass().getDeclaredFields();
            TableData tableData = tables.get(((Entity) object.getClass().getAnnotation(Entity.class)).name());
            StringBuilder build = new StringBuilder("INSERT INTO " + ((Entity) object.getClass().getAnnotation(Entity.class)).name() + tableData.getSQLAttributes() + " VALUES(");
            boolean first = true;
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Attribute.class)) {
                    if (!first) build.append(",'" + field.get(object).toString() + "'");
                    else {
                        first = false;
                        build.append("'" + field.get(object).toString() + "'");
                    }
                }
            }
            build.append(");");
            statement.execute(build.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void updateObjectInTable(Object objOld, String attr, String newData, String attrOfCon) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            Field[] fields = objOld.getClass().getDeclaredFields();
            Field f = null;
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if(fields[i].getAnnotation(Attribute.class).name().equals(attrOfCon)) {
                    f = fields[i];
                    break;
                }
            }
            if (!f.getAnnotation(Attribute.class).type().equals("INTEGER")) newData = "'"+newData+"'";
            TableData tableData = tables.get(((Entity) objOld.getClass().getAnnotation(Entity.class)).name());
            StringBuilder build = new StringBuilder("UPDATE " + tableData.getName() + " SET " + attr + " = " + newData + " WHERE " + attrOfCon + "='" + f.get(objOld).toString() + "';");
            statement.execute(build.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void deleteObjectInTable(Object object, String attrOfCon) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            Field[] fields = object.getClass().getDeclaredFields();
            Field f = null;
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if(fields[i].getAnnotation(Attribute.class).name().equals(attrOfCon)) {
                    f = fields[i];
                    break;
                }
            }
            TableData tableData = tables.get(((Entity) object.getClass().getAnnotation(Entity.class)).name());
            StringBuilder build = new StringBuilder("DELETE FROM " + tableData.getName() + " WHERE " + attrOfCon + "='" + f.get(object).toString() + "';");
            statement.execute(build.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
