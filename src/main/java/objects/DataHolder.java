package objects;

import java.util.ArrayList;

public class DataHolder {
    public ArrayList<Project> projectsList = new ArrayList<Project>();
    public ArrayList<Notes> notesList = new ArrayList<Notes>();

    public DataHolder() {projectsList = new ArrayList<Project>();}

    /*Setters, getters*/
    public void setProjects(ArrayList projects) {
        projectsList = projects;
    }
    public ArrayList<Project> getProjectsList() {
        return projectsList;
    }
    public ArrayList<Notes> getNotesList() {
        return notesList;
    }
    public void setNotesList(ArrayList<Notes> notesList) {
        this.notesList = notesList;
    }
}
