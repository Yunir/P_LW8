package main;

import objects.AimsHolder;
import objects.ProjectsHolder;

/**
 * Created by Yunicoed on 31.05.2017.
 */
public class DataModel {
    private ProjectsHolder projects;
    private AimsHolder aims;

    public DataModel() {
        projects = new ProjectsHolder();
        aims = new AimsHolder();
    }

    /*Constructors*/
    public ProjectsHolder getProjects() {
        return projects;
    }

    public void setProjects(ProjectsHolder projects) {
        this.projects = projects;
    }


    public AimsHolder getAims() {
        return aims;
    }

    public void setAims(AimsHolder aims) {
        this.aims = aims;
    }

}
