package objects.TableviewObservableLists;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Notes;

public class NotesHolder {
    private ObservableList<Notes> notesObsList;

    public NotesHolder() {
        notesObsList = FXCollections.observableArrayList();
    }

    public void create(String s, int i) {
        notesObsList.add(new Notes(s, i));
        System.out.println(notesObsList.get(notesObsList.size()-1).getText() + " added successfully.");
    }
    public void update() {}
    public void delete() {}


    /*Getters, setters*/
    public void setNotesObsList(ObservableList<Notes> notesObsList) {this.notesObsList = notesObsList;}
    public ObservableList<Notes> getNotesObsList() {return notesObsList;}
}
