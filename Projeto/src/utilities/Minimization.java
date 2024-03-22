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
        // List<Transition> transicoes = utilities.Arquivo.listaTransicoes(doc);
        this.marcarTrivialmenteNaoEquivalente(paresEstados, estados);
        this.marcarNaoEquivalente(paresEstados, estados, arquivo, validarAutomato);
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
    public void marcarNaoEquivalente(HashMap<String, CombinedState> paresEstados, List<State> estados, Arquivo arquivo, ValidarAutomato validarAutomato) {
        List<String> sigma = new ArrayList<>(validarAutomato.getSigma());
        List<State> listaPuPv = new ArrayList<>();
        String chaveEstado, chavePuPv;
        Comparator<State> comparador = Comparator.comparing(State::getId);
        int x = 0;
        for (int i = 0; i < estados.size(); i++) {
            for (int j = i + 1; j < estados.size(); j++) {
                chaveEstado = Integer.toString(estados.get(i).getId()) + Integer.toString(estados.get(j).getId());
                if (!paresEstados.get(chaveEstado).isEquivalent()) {
                    continue;
                }
                for(int k = 0; k < sigma.size(); k++){
                        this.acharPuPv(listaPuPv, paresEstados.get(chaveEstado).getQu(), paresEstados.get(chaveEstado).getQv(),
                                sigma.get(k), arquivo);
                        Collections.sort(listaPuPv, comparador);
                        if(listaPuPv.get(0).getId() != listaPuPv.get(1).getId()){
                            chavePuPv = Integer.toString(listaPuPv.get(0).getId()) + Integer.toString(listaPuPv.get(1).getId());
                            if(paresEstados.get(chavePuPv).isEquivalent()){   // 3.2
                                paresEstados.get(chavePuPv).setListaEstados(paresEstados.get(chaveEstado));
                                
                            }else{      // 3.3
                                
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
                    if(achouPuPv){
                        break;
                    }else{
                        achouPuPv = true;
                    }
                }
            }
            if (transition.getFrom() == qv.getId()) {
                if (transition.getRead().equals(simbolo)) {
                    listaPuPv.add(listaEstStates.get(transition.getTo()));
                    if(achouPuPv){
                        break;
                    }else{
                        achouPuPv = true;
                    }
                }             
            }
        }

    }

}
