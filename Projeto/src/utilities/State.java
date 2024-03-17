package utilities;

import java.util.ArrayList;

public class State {
    private int id;
    private String name;
    private ArrayList<Transition> listTransitions;
    private Boolean isInitial;
    private Boolean isFinal;
    
    public State(int id, String name, ArrayList<Transition> listTransitions, Boolean isInitial, Boolean isFinal) {
        this.id = id;
        this.name = name;
        this.listTransitions = listTransitions;
        this.isInitial = isInitial;
        this.isFinal = isFinal;
    }

    public Boolean getIsInitial() {
        return isInitial;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public void setListTransitions(ArrayList<Transition> listTransitions) {
        this.listTransitions = listTransitions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Transition> getListTransitions() {
        return listTransitions;
    }
}
