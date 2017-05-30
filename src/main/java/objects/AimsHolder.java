package objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

public class AimsHolder {
    private ObservableList<Aim> Aims;
    public ObservableList<Aim> getAimsObsList() {return Aims;}
    public void setAimsObsList(ObservableList<Aim> projects) {Aims = projects;}

    public AimsHolder() {
        Aims = FXCollections.observableArrayList();
    }

    public void create(Aim o) {
        Aims.add(o);
        System.out.println(Aims.get(Aims.size()-1).getName() + " added successfully.");
    }

    public void read() {

    }

    public void update() {

    }

    public void delete() {

    }

    public void exampleInitData(int i) {
        for (int j = 0; j < i; j++) {
            create(new Aim(j + ". really important aim"));
        }
    }

}
