package objects;

import java.util.ArrayList;

public class DataHolder {
    public ArrayList<Project> projectsList = new ArrayList<Project>();

    public DataHolder() {projectsList = new ArrayList<Project>();}

    /*Setters, getters*/
    public void setProjects(ArrayList projects) {
        projectsList = projects;
    }
    public ArrayList<Project> getProjectsList() {
        return projectsList;
    }
}
