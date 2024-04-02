package utilities;

import java.util.Set;
import java.util.HashSet;

public class Estado {
	private String id;
	private String name;
	private double x;
	private double y;
	private boolean isInitial;
	private boolean isFinal;
	private Set<String> label;

	public void setLabel(Set<String> label) {
		this.label = label;
	}

	public Set<String> getLabel() {
		if (label == null)
			return new HashSet<>();
		return label;
	}

	public Estado copyWith() {
		Estado copyWith = new Estado(id, name, x, y, isInitial, isFinal);
		copyWith.label = label;
		return copyWith;
	}

	public Estado() {
	}

	public Estado(String id, String name) {
		setId(id);
		setName(name);
	}

	public Estado(String id, String name, double x, double y, boolean isInitial, boolean isFinal, Set<String> label) {
		this(id, name, x, y, isInitial, isFinal);
		this.label = label;
		if (label == null)
			setLabel(new HashSet<>());
	}

	public Estado(String id, String name, double x, double y, boolean isInitial, boolean isFinal) {
		this(id, name);
		setX(x);
		setY(y);
		setInitial(isInitial);
		setFinal(isFinal);
	}

	String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	double getX() {
		return x;
	}

	void setX(double x) {
		this.x = x;
	}

	double getY() {
		return y;
	}

	void setY(double y) {
		this.y = y;
	}

	boolean isInitial() {
		return isInitial;
	}

	void setInitial(boolean isInitial) {
		this.isInitial = isInitial;
	}

	boolean isFinal() {
		return isFinal;
	}

	void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("ID: %s\n", getId()));
		sb.append(String.format("Name: %s\n", getName()));
		sb.append(String.format("X: %f\n", getX()));
		sb.append(String.format("Y: %f\n", getY()));
		sb.append(String.format("Inicial: %s\n", isInitial()));
		sb.append(String.format("Final: %s\n", isFinal()));
		if (label != null && label.size() > 0) {
			sb.append("Label: ");
			for (String string : label) {
				sb.append(string + " | ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}