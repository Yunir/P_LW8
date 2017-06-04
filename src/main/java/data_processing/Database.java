package data_processing;

import objects.Aim;
import objects.Project;

import java.sql.*;
import java.util.ArrayList;
import static main.Main.dataHolder;

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
  /*  public void activateQuery(String q) throws SQLException {
        Statement statement = null;
        String query = q;

        try {
            statement = dbConnection.createStatement();

            // выполнить SQL запрос
            statement.execute(query);
            System.out.println("Query successfully finished.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            *//*if (dbConnection != null) {
                dbConnection.close();
            }*//*
        }
    }*/

    /*Getters, setters*/
    public Connection getDbConnection() {return dbConnection;}
}
