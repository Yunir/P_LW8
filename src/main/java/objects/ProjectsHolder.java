package objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProjectsHolder {
    private ObservableList<Project> ProjectsObsList;

    public ObservableList<Project> getProjectsObsList() {
        return ProjectsObsList;
    }

    public void setProjectsObsList(ObservableList<Project> projectsObsList) {
        ProjectsObsList = projectsObsList;
    }


    public ProjectsHolder() {
        ProjectsObsList = FXCollections.observableArrayList();
    }

    public void create(Project o) {
        ProjectsObsList.add(o);
        System.out.println(ProjectsObsList.get(ProjectsObsList.size()-1).getName() + " added successfully.");
    }

    public void read() {

    }

    public void update() {

    }

    public void delete() {

    }

    /*public void exampleInitData() {
        create(new Project("Fast and furious"));
        create(new Project("Great and bright"));
        create(new Project("Light and slow"));
    }*/
}
