package utilities;

import java.util.Set;
import java.util.HashSet;

public class EstadoComTransicoes {
    private Estado estado;
    private Set<Transicao> transicao;

    public EstadoComTransicoes(Estado estado) {
        this.estado = estado;
        transicao = new HashSet<>();
    }

    public void setTransicao(Set<Transicao> transicao) {
        this.transicao = transicao;
    }

    public Set<Transicao> getTransicao() {
        return transicao;
    }

    public Estado getEstado() {
        return estado;
    }

    public Set<String> getLabelDoEstado() {
        return estado.getLabel();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n### ESTADO ###\n");
        sb.append(String.format("ID: %s\n", estado.getId()));
        sb.append(String.format("Name: %s\n", estado.getName()));
        sb.append(String.format("X: %f\n", estado.getX()));
        sb.append(String.format("Y: %f\n", estado.getY()));
        sb.append(String.format("Inicial: %s\n", estado.isInitial()));
        sb.append(String.format("Final: %s\n", estado.isFinal()));
        if (estado.getLabel() != null && estado.getLabel().size() > 0) {
            sb.append("Label: ");
            for (String string : estado.getLabel()) {
                sb.append(string + " | ");
            }
            sb.append("\n");
        }
        sb.append("### TRANSICOES ###\n");

        if (transicao != null) {
            for (Transicao transicao : transicao) {
                sb.append(transicao.toString());
            }
        }
        return sb.toString();
    }
}
