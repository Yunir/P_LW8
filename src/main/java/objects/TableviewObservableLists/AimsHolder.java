package objects.TableviewObservableLists;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Aim;

import java.io.Serializable;

public class AimsHolder {
    private ObservableList<Aim> aimsObsList;

    public AimsHolder() {
        aimsObsList = FXCollections.observableArrayList();
    }

    //Commands
    public void create(Aim o) {
        aimsObsList.add(o);
        System.out.println(aimsObsList.get(aimsObsList.size()-1).getName() + " added successfully.");
    }
    public void read() {}
    public void update() {}
    public void delete() {}


    /*Getters, setters*/
    public void setAimsObsList(ObservableList<Aim> aimsObsList) {this.aimsObsList = aimsObsList;}
    public ObservableList<Aim> getAimsObsList() {return aimsObsList;}

}
