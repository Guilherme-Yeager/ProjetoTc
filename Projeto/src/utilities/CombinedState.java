package utilities;

public class CombinedState {
    private State qu;
    private State qv;
    private boolean isEquivalent;

    
    public CombinedState(State qu, State qv){
        this.qu = qu;
        this.qv = qv;
        this.isEquivalent = true;
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
