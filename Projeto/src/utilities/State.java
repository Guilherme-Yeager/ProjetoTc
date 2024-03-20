package utilities;

public class State {
    private int id;
    private String name;
    private float x, y;
    private Boolean isInitial;
    private Boolean isFinal;
    private String label;

    public State(int id, String name, Boolean isInitial, Boolean isFinal, float x, float y, String label) {
        this.id = id;
        this.name = name;
        this.isInitial = isInitial;
        this.isFinal = isFinal;
        this.x = x;
        this.y = y;
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Boolean getIsInitial() {
        return isInitial;
    }

    public void setIsInitial(Boolean isInitial) {
        this.isInitial = isInitial;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
    }

}
