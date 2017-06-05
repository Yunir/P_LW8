package objects;

import java.util.ArrayList;

import static controllers.MainController.projectsHolder;

public class DataHolder {
    public ArrayList<Project> projectsList = new ArrayList<Project>();

    public DataHolder() {projectsList = new ArrayList<Project>();}

    /*Getters, setters*/
    public ArrayList<Project> getProjects() {return projectsList;}
    public void setProjects(ArrayList projects) {
        projectsList = projects;
    }
    public void showAllProjects () {
        for (int i = 0; i < projectsList.size(); i++) {
            System.out.println(projectsList.get(i).getName());
        }
    }

    public void addProject(String nameOfProject) {
        projectsList.add(new Project(nameOfProject, 0));
    }
}
