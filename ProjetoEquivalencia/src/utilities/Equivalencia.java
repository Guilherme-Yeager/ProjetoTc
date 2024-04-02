package utilities;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Equivalencia {
    public Equivalencia(Automato automatoLido) {
        this.automatoLido = automatoLido;
    }

    private Automato automatoLido;
    private ArrayList<Estado> estadosList;
    private int contParaPegarXY = 0;
    private double posicaoX = 200, posicaoY = 200;
    private ArrayList<EstadoComTransicoes> auxAutomatoSaida = new ArrayList<EstadoComTransicoes>();
    private final String LAMBDA = "";

    public void gerarAutomato() {
        gerarEstadoInicial();
        fazerEquivalencia();
    }

    public void fazerEquivalencia() {
        for (int i = 0; i < auxAutomatoSaida.size(); i++) {
            Map<String, Set<String>> possiveisNovosEstados = pegarPossiveisNovoEstado(i);
            Set<String> readChaves = possiveisNovosEstados.keySet();
            if (readChaves == null || readChaves.isEmpty())
                continue;

            for (String read : readChaves) {
                Set<String> auxReadLabel = possiveisNovosEstados.get(read);
                if (temNovoEstadoParaCriar(auxReadLabel)) {
                    criarNovoEstado(auxReadLabel);
                }
            }
            addTransicaoDoEstadoAtualSeTiver(i, possiveisNovosEstados, readChaves);
        }
    }

    public void addTransicaoDoEstadoAtualSeTiver(int index, Map<String, Set<String>> possiveisNovosEstados,
            Set<String> readChaves) {
        EstadoComTransicoes saidaFrom = auxAutomatoSaida.get(index);
        for (String read : readChaves) {
            for (EstadoComTransicoes saidaTo : auxAutomatoSaida) {
                if (saidaTo.getLabelDoEstado().equals(possiveisNovosEstados.get(read))) {
                    String from = saidaFrom.getEstado().getId();
                    String to = saidaTo.getEstado().getId();
                    Transicao t = new Transicao(from, to, read);
                    saidaFrom.getTransicao().add(t);
                }
            }
        }
    }

    public void criarNovoEstado(Set<String> label) {
        String novoId = String.valueOf(auxAutomatoSaida.size());
        double x, y;
        if (contParaPegarXY < estadosList.size() - 1) {
            x = estadosList.get(contParaPegarXY).getX();
            y = estadosList.get(contParaPegarXY).getY();
            contParaPegarXY++;
        } else {
            x = posicaoX;
            y = posicaoY;
            posicaoX += 200;
            posicaoY += 200;
        }
        Estado novoEstado = new Estado(novoId, "q" + novoId, x, y, false, false, label);
        EstadoComTransicoes et = new EstadoComTransicoes(novoEstado);

        for (Estado e : automatoLido.getEstados()) {
            if (label.contains(e.getId()) && e.isFinal()) {
                et.getEstado().setFinal(true);
                break;
            }
        }
        auxAutomatoSaida.add(et);
    }

    public Map<String, Set<String>> pegarPossiveisNovoEstado(int index) {
        Map<String, Set<String>> possiveisNovosEstados = new HashMap<>();
        Set<String> auxLabel = auxAutomatoSaida.get(index).getEstado().getLabel();
        if (auxLabel != null && auxLabel.size() > 0) {
            for (String id : auxLabel) {
                Estado estadoLido = automatoLido.getEstadoPorId(id);
                Set<Transicao> transicoesLido = automatoLido.getTransicoesPorEstado(estadoLido);
                for (Transicao t : transicoesLido) {
                    if (!t.getRead().equals(LAMBDA)) {
                        if (possiveisNovosEstados.get(t.getRead()) == null)
                            possiveisNovosEstados.put(t.getRead(), new HashSet<>());
                        possiveisNovosEstados.put(t.getRead(), addLabelsRecursivoComLetra(
                                possiveisNovosEstados.get(t.getRead()), estadoLido, t.getRead()));
                    }
                }
            }
        }
        return possiveisNovosEstados;
    }

    public boolean temNovoEstadoParaCriar(Set<String> novoEstado) {
        if (novoEstado == null || novoEstado.size() == 0)
            return false;
        for (EstadoComTransicoes et : auxAutomatoSaida) {
            if (et.getEstado().getLabel().equals(novoEstado))
                return false;
        }
        return true;
    }

    public Set<String> addLabelsRecursivoComLetra(Set<String> label, Estado novoEstado, String letra) {
        Set<Transicao> transicoesNovoEstado = automatoLido.getTransicoesPorEstado(novoEstado);
        if (transicoesNovoEstado != null && transicoesNovoEstado.size() > 0) {
            for (Transicao t : transicoesNovoEstado) {
                if (t.getRead().equals(letra) && !label.contains(t.getTo())) {
                    label.add(t.getTo());
                    label = addLabelsRecursivoComLetra(label, automatoLido.getEstadoPorId(t.getTo()), LAMBDA);
                }
            }
        }
        return label;
    }

    public void gerarEstadoInicial() {
        Estado primeiroEstado = automatoLido.getEstadoInicial().copyWith();
        if (primeiroEstado == null || !primeiroEstado.isInitial())
            Error.errorMessageAndExit("O automato não tem estado inicial. Não foi possível fazer a equivalência");
        primeiroEstado.setId(String.valueOf(auxAutomatoSaida.size())); // Sempre vai ser 0, pois não tem nenhum lá dentro
                                                                       // ainda
        primeiroEstado.setName("q" + String.valueOf(auxAutomatoSaida.size()));
        EstadoComTransicoes auxAutomatoInicial = new EstadoComTransicoes(primeiroEstado);

        auxAutomatoInicial.getEstado().setLabel(
                addLabelsRecursivoComLetra(new HashSet<>(Set.of("0")), auxAutomatoInicial.getEstado(), LAMBDA));

        Set<String> label = auxAutomatoInicial.getLabelDoEstado();
        for (Estado e : automatoLido.getEstados()) {
            if (label.contains(e.getId()) && e.isFinal()) {
                auxAutomatoInicial.getEstado().setFinal(true);
                break;
            }
        }
        contParaPegarXY++;
        estadosList = new ArrayList<>(automatoLido.getEstados());
        auxAutomatoSaida.add(auxAutomatoInicial);
    }

    public Automato getAutomatoGerado() {
        Automato automato = new Automato();
        for (EstadoComTransicoes estadoComTransicoes : auxAutomatoSaida) {
            automato.addEstado(estadoComTransicoes.getEstado());
            Set<Transicao> t = estadoComTransicoes.getTransicao();
            if (t != null) {
                for (Transicao transicao : t) {
                    automato.addTransicao(transicao);
                }
            }
        }
        return automato;
    }
}
