package data_processing;

import com.sun.rowset.FilteredRowSetImpl;
import objects.Aim;
import objects.Project;

import javax.sql.rowset.FilteredRowSet;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Vector;

import static general_classes.Main.dataHolder;

public class Database {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/ProjectsDB";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    private Connection dbConnection;

    public Database() {
        getDBConnection();
        try {
            activateQuery("CREATE TABLE IF NOT EXISTS realized_objects (ID SERIAL PRIMARY KEY, name VARCHAR(256));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getDBConnection() {
        dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {System.out.println(e.getMessage());}
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {System.out.println(e.getMessage());}
    }

    public void getFullDB() {
        Statement statement;
        Statement subStatement;
        ResultSet resultSet;
        ResultSet subResultSet;
        try {
            String query = "SELECT * FROM projectholder ORDER BY id;";
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(query);
            ArrayList<Project> projectList = new ArrayList<>();
            while (resultSet.next()) {
                String subquery = "SELECT id, aim, priority FROM aimholder where project_id = "+ resultSet.getInt(1) +" ORDER BY id;";
                subStatement = dbConnection.createStatement();
                subResultSet = subStatement.executeQuery(subquery);
                Project project = new Project(resultSet.getString(2).replace("  ",""),resultSet.getInt(3), OffsetDateTime.now());
                while(subResultSet.next()) {
                    project.getAimsList().add(new Aim(subResultSet.getString(2).replace("  ",""),subResultSet.getInt(3), OffsetDateTime.now()));
                }
                projectList.add(project);
                dataHolder.setProjects(projectList);
                subStatement.close();
                subResultSet.close();
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProject(String[] name) {
        try {
            activateQuery("BEGIN;");
            for (int i = 0; i < name.length; i++) {

                activateQuery("INSERT INTO projectholder (project) VALUES(\'" + name[i] + "\');");
            }
            activateQuery("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addAim(String s, String s1, int priority) {
        try {
            activateQuery("INSERT INTO aimholder (project_id, aim, priority) VALUES ((SELECT id FROM projectholder WHERE project = \'"+s+"\'),\'"+s1+"\',"+priority+");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateProject(String oldName, String newName) {
        try {
            activateQuery("UPDATE projectholder SET project = \'"+newName+"\' WHERE project = \'"+oldName+"\';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateAim(String project, String oldName, String newName, int priority) {
        try {
            activateQuery("UPDATE aimholder SET aim = \'"+newName+"\', priority = "+priority+" WHERE project_id = (SELECT id FROM projectholder WHERE project = \'"+project+"\') AND aim = \'"+oldName+"\';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteProject(String name) {
        try {
            activateQuery("DELETE FROM projectholder WHERE project = \'"+name+"\';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAim(String project, String aim) {

        try {
            activateQuery("DELETE FROM aimholder WHERE project_id = (SELECT id FROM projectholder WHERE project = \'"+project+"\') AND aim = \'"+aim+"\';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int findSimilarProjects(String name) {
        try {
            Statement statement;
            ResultSet rs;
            String query = "select count(id) from projectholder where project ~* \'^"+ name +"$\';";
            statement = dbConnection.createStatement();
            rs = statement.executeQuery(query);
            FilteredRowSet frs = new FilteredRowSetImpl();
            frs.populate(rs);
            System.out.println("Query successfully completed");
            while (frs.next()){
                return frs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int findSimilarAims(String project, String aim) {
        try {
            Statement statement;
            ResultSet rs;
            String query = "SELECT count(id) FROM aimholder WHERE aim = \'"+aim+"\' AND project_id = (SELECT id FROM projectholder WHERE project = \'"+project+"\');";
            statement = dbConnection.createStatement();
            rs = statement.executeQuery(query);
            FilteredRowSet frs = new FilteredRowSetImpl();
            frs.populate(rs);
            System.out.println("Query successfully completed");
            while (frs.next()){
                return frs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*Private Methods*/
    private void activateQuery(String q) throws SQLException {
        Statement statement;
        statement = dbConnection.createStatement();
        statement.execute(q);
        System.out.println("Query successfully completed");
    }

    public synchronized void newObjectTable(Class aClass) {
        Statement statement;
        try {
            String nameOfClass = aClass.getName().replace('.', '_');
            statement = dbConnection.createStatement();
            Vector<String> realized_objects_vector = new Vector<>();
            boolean canCreate = true;
            ResultSet rs = statement.executeQuery("SELECT DISTINCT name FROM realized_objects; ");
            while (rs.next()){
                realized_objects_vector.add(rs.getString(1));
            }
            for(String already_created : realized_objects_vector){
                if(already_created.equals(nameOfClass)){
                    canCreate=false;
                    break;
                }
            }
            if ((!(aClass == null))&&(canCreate)) {
                Field[] fields = aClass.getDeclaredFields();
                String[] attributes = new String[fields.length];
                String[] types = new String[fields.length];
                for(int i=0;i<fields.length;i++){
                    attributes[i] = fields[i].getName();
                    System.out.println(fields[i].getType().getName());
                    if(fields[i].getType().getName().equals("java.time.OffsetDateTime")){
                        System.out.println(fields[i].getType().getName().equals("java.time.OffsetDateTime"));
                        types[i]="varchar(50)";}
                    else if(fields[i].getType().getName().equals("java.lang.String")){types[i]="varchar(50)";}
                    else if(fields[i].getType().getName().equals("boolean")){types[i]="Boolean";}
                    else if(fields[i].getType().getName().equals("int")){types[i]="Integer";}
                    else types[i]="next";;
                }

                StringBuilder build = new StringBuilder("CREATE TABLE IF NOT EXISTS "+nameOfClass+" (ID SERIAL PRIMARY KEY,");
                for(int i=0;i<attributes.length;i++){
                    if(types[i].equals("next")) continue;
                    String append =" "+attributes[i]+" "+types[i]+",";
                    build.append(append);
                }
                build.deleteCharAt(build.length()-1);
                build.append(");");
                String superQuery = build.toString();
                System.out.println("my query " + superQuery);
                statement.execute(superQuery);
                statement.execute("INSERT INTO realized_objects VALUES (DEFAULT ,'" +nameOfClass+"');");

            }
        }catch (Exception e){}
    }
}
