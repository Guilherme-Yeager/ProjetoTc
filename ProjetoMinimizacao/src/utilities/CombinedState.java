package utilities;

import java.util.HashSet;
import java.util.Set;

public class CombinedState {
    private State qu;
    private State qv;
    private boolean isEquivalent;
    private Set<CombinedState> listaEstados;
    
    
    public CombinedState(State qu, State qv){
        this.qu = qu;
        this.qv = qv;
        this.isEquivalent = true;
        this.listaEstados = new HashSet<>();
    }
    
    public Set<CombinedState> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(CombinedState estado) {
        this.listaEstados.add(estado);
    }

    public State getQu() {
        return qu;
    }
    public void setQu(State qu) {
        this.qu = qu;
    }
    public State getQv() {
        return qv;
    }
    public void setQv(State qv) {
        this.qv = qv;
    }
    
    public boolean isEquivalent() {
        return isEquivalent;
    }
    
    public void setEquivalent(boolean isEquivalent) {
        this.isEquivalent = isEquivalent;
    }
    
}
