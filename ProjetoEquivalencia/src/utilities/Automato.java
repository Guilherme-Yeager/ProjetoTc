package utilities;

import java.util.HashSet;
import java.util.Set;

public class Automato {
	private Set<Estado> estados;
	private Set<Transicao> transicoes;
	private Estado estadoInicial;

	public Automato() {
		setEstados(new HashSet<>());
		setTransicoes(new HashSet<>());
	}

	Estado getEstadoInicial() {
		return estadoInicial;
	}

	void setEstadoInicial(Estado estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	void addEstadoInicial(Estado estado) {
		if (estadoInicial == null)
			estadoInicial = estado;
	}

	void addTransicao(Transicao t) {
		if (transicoes == null)
			setTransicoes(new HashSet<>());
		transicoes.add(t);
	}

	void setTransicoes(Set<Transicao> transicoes) {
		this.transicoes = transicoes;
	}

	Set<Transicao> getTransicoes() {
		return transicoes;
	}

	Set<Estado> getEstados() {
		return estados;
	}

	void setEstados(Set<Estado> estados) {
		this.estados = estados;
	}

	void addEstado(Estado e) {
		if (getEstados() == null)
			setEstados(new HashSet<>());
		estados.add(e);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Automato:\n");
		for (Estado e : getEstados()) {
			sb.append(e);
		}
		sb.append("Transições: \n");
		for (Transicao t : getTransicoes()) {
			sb.append(t);
		}
		return sb.toString();
	}

	Estado getEstadoPorId(String id) {
		for (Estado e : getEstados()) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	Set<Transicao> getTransicoesPorEstado(Estado e) {
		Set<Transicao> transicoes = new HashSet<>();
		for (Transicao t : getTransicoes()) {
			if (t.getFrom().equals(e.getId()))
				transicoes.add(t);
		}
		return transicoes;
	}

	public static String retornarLabelComVirgula(Set<String> str) {
		if (str == null || str.size() == 0)
			return "";
		StringBuilder labelComVirgula = new StringBuilder();
		for (String label : str) {
			if (labelComVirgula.length() > 0)
				labelComVirgula.append(",");
			labelComVirgula.append(label);
		}
		return labelComVirgula.toString();
	}
}