package data_processing;

import objects.Aim;
import objects.Project;

import java.sql.*;
import java.util.ArrayList;
import static general_classes.Main.dataHolder;

public class Database {
    static final String DB_DRIVER = "org.postgresql.Driver";
    static final String DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/ProjectsDB";
    static final String DB_USER = "postgres";
    static final String DB_PASSWORD = "admin";

    private Connection dbConnection;

    public Database() {
        getDBConnection();
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
            ArrayList<Project> projectList = new ArrayList<Project>();
            while (resultSet.next()) {
                String subquery = "SELECT id, aim, priority FROM aimholder where project_id = "+ resultSet.getInt(1) +" ORDER BY id;";
                subStatement = dbConnection.createStatement();
                subResultSet = subStatement.executeQuery(subquery);
                Project project = new Project(resultSet.getString(2).replace("  ",""),resultSet.getInt(3));
                while(subResultSet.next()) {
                    project.getAimsList().add(new Aim(subResultSet.getString(2).replace("  ",""),subResultSet.getInt(3)));
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

    public void insertProject(String name) {
        try {
            activateQuery("INSERT INTO projectholder (project) VALUES(\'" + name + "\');");
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
            Statement statement = null;
            ResultSet rs = null;
            String query = "select count(id) from projectholder where project ~* \'^"+ name +"$\';";
            statement = dbConnection.createStatement();
            rs = statement.executeQuery(query);
            System.out.println("Query successfully completed");
            while (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int findSimilarAims(String project, String aim) {
        try {
            Statement statement = null;
            ResultSet rs = null;
            String query = "SELECT count(id) FROM aimholder WHERE aim = \'"+aim+"\' AND project_id = (SELECT id FROM projectholder WHERE project = \'"+project+"\');";
            statement = dbConnection.createStatement();
            rs = statement.executeQuery(query);
            System.out.println("Query successfully completed");
            while (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*Private Methods*/
    private void activateQuery(String q) throws SQLException {
        Statement statement = null;
        ResultSet rs = null;
        String query = q;
        statement = dbConnection.createStatement();
        statement.execute(query);
        System.out.println("Query successfully completed");
    }

    /*Getters, setters*/
    public Connection getDbConnection() {return dbConnection;}
}
