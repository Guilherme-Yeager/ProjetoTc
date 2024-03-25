package utilities;

public class Transition {
    private int from, to;
    private String read;
    
    public Transition(int from, int to, String read) {
        this.from = from;
        this.to = to;
        this.read = read;
    }
    
    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public String getRead() {
        return read;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setRead(String read) {
        this.read = read;
    }


}