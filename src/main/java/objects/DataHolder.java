package objects;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import static controllers.MainController.projectsHolder;

public class DataHolder {
    public ArrayList<Project> projectsList = new ArrayList<Project>();
    public volatile ArrayList<Notes> notesList = new ArrayList<>();
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
        projectsList.add(new Project(nameOfProject, 0, OffsetDateTime.now()));
    }
    public ArrayList<Notes> getNotesList() {
        return notesList;
    }
    public void setNotesList(ArrayList<Notes> notesList) {
        this.notesList = notesList;
    }

    public void addNote(String text, int i) {
        notesList.add(new Notes(text, i));
    }
}
