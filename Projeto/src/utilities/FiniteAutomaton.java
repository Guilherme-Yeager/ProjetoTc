package utilities;

import java.util.ArrayList;

public class FiniteAutomaton {
    private ArrayList<State> listStates;
    
    private int qtdState;
    private Boolean isAfd;

    public FiniteAutomaton(ArrayList<State> listStates) {
        this.listStates = listStates;
    }
}
