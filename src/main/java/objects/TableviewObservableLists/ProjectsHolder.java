package objects.TableviewObservableLists;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Project;

public class ProjectsHolder {
    private ObservableList<Project> ProjectsObsList;

    public ProjectsHolder() { ProjectsObsList = FXCollections.observableArrayList(); }

    //Commands
    public void create(Project project) {
        ProjectsObsList.add(project);
        System.out.println(ProjectsObsList.get(ProjectsObsList.size()-1).getName() + " added successfully.");
    }
    public void read() {}
    public void update() {}
    public void delete() {}

    /*Getters, setters*/
    public ObservableList<Project> getProjectsObsList() {
        return ProjectsObsList;
    }
    public void setProjectsObsList(ObservableList<Project> projectsObsList) {ProjectsObsList = projectsObsList;}
    public void showAllProjects() {
        for (int i = 0; i < ProjectsObsList.size(); i++) {
            System.out.println(ProjectsObsList.get(i).getName());
        }
    }
}
