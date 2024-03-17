package file;

public class Transition {
    private int id;
    private String from;
    private String to;
    private String read;

    public int getId() {
        return id;
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public String getRead() {
        return read;
    }

    public Transition(int id, String from, String to, String read) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.read = read;
    }
}