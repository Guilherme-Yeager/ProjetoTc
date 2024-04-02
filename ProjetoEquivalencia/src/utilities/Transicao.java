package utilities;

public class Transicao {
	private String from;
	private String to;
	private String read;

	public Transicao() {
	}

	public Transicao(String from, String to, String read) {
		setFrom(from);
		setTo(to);
		setRead(read);
	}

	String getFrom() {
		return from;
	}

	void setFrom(String from) {
		this.from = from;
	}

	String getTo() {
		return to;
	}

	void setTo(String to) {
		this.to = to;
	}

	String getRead() {
		return read;
	}

	private void setRead(String read) {
		this.read = read;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("From: %s -> To: %s, Read: %s \n", getFrom(), getTo(), getRead()));
		return sb.toString();
	}
}