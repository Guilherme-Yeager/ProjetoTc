package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Minimization {

    public void minimizarAutomato(Arquivo arquivo, ValidarAutomato validarAutomato) {
        List<State> estados = arquivo.listaEstados(arquivo.getDoc());
        HashMap<String, CombinedState> paresEstados = this.geraTabela(estados);
        this.marcarTrivialmenteNaoEquivalente(paresEstados, estados);
        this.marcarNaoEquivalente(paresEstados, estados, arquivo, validarAutomato);

        List<State> novosEstados = this.unificarEstados(paresEstados, estados);
        List<Transition> transicoes = arquivo.listaTransicoes(arquivo.getDoc());
        List<Transition> novasTransicoes = this.unificarTransicoes(novosEstados, transicoes, estados);
        arquivo.gravarAutomato(novosEstados, novasTransicoes);

    }

    // Passo 1
    public HashMap<String, CombinedState> geraTabela(List<State> estados) {
        HashMap<String, CombinedState> paresEstados = new HashMap<>();
        for (int i = 0; i < estados.size(); i++) { // Gerar a combinação de pares de autômatos (tabela), passo 1
            for (int j = i + 1; j < estados.size(); j++) {
                paresEstados.put(Integer.toString(estados.get(i).getId()) + Integer.toString(estados.get(j).getId()),
                        new CombinedState(estados.get(i), estados.get(j)));
            }
        }
        return paresEstados;
    }

    // Passo 2
    public void marcarTrivialmenteNaoEquivalente(HashMap<String, CombinedState> paresEstados, List<State> estados) {
        for (int i = 0; i < estados.size(); i++) { // Gerar a combinação de pares de autômatos (tabela), passo 1
            for (int j = i + 1; j < estados.size(); j++) {
                String chave = Integer.toString(estados.get(i).getId()) + Integer.toString(estados.get(j).getId());
                if (paresEstados.get(chave).getQu().getIsFinal()) {
                    if (!paresEstados.get(chave).getQv().getIsFinal()) {
                        paresEstados.get(chave).setEquivalent(false);
                    }
                } else {
                    if (paresEstados.get(chave).getQv().getIsFinal()) {
                        paresEstados.get(chave).setEquivalent(false);
                    }
                }
            }
        }
    }

    // Passo 3
    public void marcarNaoEquivalente(HashMap<String, CombinedState> paresEstados, List<State> estados, Arquivo arquivo,
            ValidarAutomato validarAutomato) {
        List<String> sigma = new ArrayList<>(validarAutomato.getSigma());
        List<State> listaPuPv = new ArrayList<>();
        String chaveEstado, chavePuPv;
        Comparator<State> comparador = Comparator.comparing(State::getId);
        for (int i = 0; i < estados.size(); i++) {
            for (int j = i + 1; j < estados.size(); j++) {
                chaveEstado = Integer.toString(estados.get(i).getId()) + Integer.toString(estados.get(j).getId());
                if (!paresEstados.get(chaveEstado).isEquivalent()) {
                    continue;
                }
                for (int k = 0; k < sigma.size(); k++) {
                    this.acharPuPv(listaPuPv, paresEstados.get(chaveEstado).getQu(),
                            paresEstados.get(chaveEstado).getQv(),
                            sigma.get(k), arquivo);
                    Collections.sort(listaPuPv, comparador);
                    if (listaPuPv.get(0).getId() != listaPuPv.get(1).getId()) {
                        chavePuPv = Integer.toString(listaPuPv.get(0).getId())
                                + Integer.toString(listaPuPv.get(1).getId());
                        if (paresEstados.get(chavePuPv).isEquivalent()) { // 3.2
                            paresEstados.get(chavePuPv).setListaEstados(paresEstados.get(chaveEstado));
                        } else { // 3.3
                            paresEstados.get(chaveEstado).setEquivalent(false);
                            this.limparListas(paresEstados.get(chaveEstado));
                        }
                    }
                    listaPuPv.clear();
                }

            }

        }

    }

    public void acharPuPv(List<State> listaPuPv, State qu, State qv, String simbolo, Arquivo arquivo) {
        List<Transition> listaTransitions = arquivo.listaTransicoes(arquivo.getDoc());
        List<State> listaEstStates = arquivo.listaEstados(arquivo.getDoc());
        boolean achouPuPv = false;
        for (Transition transition : listaTransitions) {
            if (transition.getFrom() == qu.getId()) {
                if (transition.getRead().equals(simbolo)) {
                    listaPuPv.add(listaEstStates.get(transition.getTo()));
                    if (achouPuPv) {
                        break;
                    } else {
                        achouPuPv = true;
                    }
                }
            }
            if (transition.getFrom() == qv.getId()) {
                if (transition.getRead().equals(simbolo)) {
                    listaPuPv.add(listaEstStates.get(transition.getTo()));
                    if (achouPuPv) {
                        break;
                    } else {
                        achouPuPv = true;
                    }
                }
            }
        }

    }

    public void limparListas(CombinedState combinedState) {
        for (CombinedState state : combinedState.getListaEstados()) {
            limparListas(state);
            state.setEquivalent(false);
            state.getListaEstados().clear();
        }

    }

    // Passo 4
    public List<State> unificarEstados(HashMap<String, CombinedState> paresEstados, List<State> estados) {
        String chaveEstado;
        List<State> novosEstados = new ArrayList<>();
        for (int i = 0; i < estados.size(); i++) {
            for (int j = i + 1; j < estados.size(); j++) {
                chaveEstado = Integer.toString(estados.get(i).getId()) + Integer.toString(estados.get(j).getId());
                if (paresEstados.get(chaveEstado).isEquivalent()) {
                    novosEstados.add(new State(paresEstados.get(chaveEstado).getQu().getId(),
                            paresEstados.get(chaveEstado).getQu().getName()
                                    + paresEstados.get(chaveEstado).getQv().getName(),
                            paresEstados.get(chaveEstado).getQu().getIsInitial()
                                    || paresEstados.get(chaveEstado).getQv().getIsInitial() ? true : false,
                            paresEstados.get(chaveEstado).getQu().getIsFinal() ? true : false,
                            paresEstados.get(chaveEstado).getQu().getX(), paresEstados.get(chaveEstado).getQu().getY(),
                            Integer.toString(paresEstados.get(chaveEstado).getQu().getId())
                                    + " " + Integer.toString(paresEstados.get(chaveEstado).getQv().getId())));
                }
            }
        }
        List<State> estadosRestante = new ArrayList<>();
        boolean adicionarEstado = true;
        for (State estado : estados) {
            for (State novoEstado : novosEstados) {
                if (estado.getId() == novoEstado.getId() || estado.getId() == novoEstado.getId() + 1) {
                    adicionarEstado = false;
                    break;
                }
            }
            if (adicionarEstado) {
                estado.setLabel("");
                estadosRestante.add(estado);
            }
            adicionarEstado = true;
        }
        novosEstados.addAll(estadosRestante);
        Comparator<State> comparador = Comparator.comparing(State::getId);
        Collections.sort(novosEstados, comparador);
        return novosEstados;
    }

    public List<Transition> unificarTransicoes(List<State> novosEstados, List<Transition> transicoes, List<State> estados) {
        List<Integer> novosIds = new ArrayList<>();
        List<Integer> idsRemovidos = new ArrayList<>();
        for(State estado : novosEstados){
            novosIds.add(estado.getId());
        }
        for(State estado: estados){
            if(!novosIds.contains(estado.getId())){
                idsRemovidos.add(estado.getId());
                
            }
        }
        List<Transition> novasTransicoes = new ArrayList<>();
        for (Transition transicao : transicoes) {
            if(!idsRemovidos.contains(transicao.getFrom())){
                novasTransicoes.add(transicao);
            }
        }
        for (Transition transicao : novasTransicoes) {
            if(idsRemovidos.contains(transicao.getTo())){
                for(State estado : novosEstados){
                    if(estado.getLabel().contains(Integer.toString(transicao.getTo()))){
                        transicao.setTo(Integer.parseInt(estado.getLabel().split(" ")[0]));
                    }
                }
            }
        }
        return novasTransicoes;
    }

}
