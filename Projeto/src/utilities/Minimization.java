package utilities;

import java.util.ArrayList;
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
        for (int i = estados.size() - 1; i >= 0; i--) {
            for (int j = estados.size() - 1; j >= i + 1; j--) {
                String chave = Integer.toString(estados.get(i).getId()) + Integer.toString(estados.get(j).getId());
                for(int k = 0; k < sigma.size(); k++){
                    if (paresEstados.get(chave).isEquivalent()) {
                        List<State> listaPuPv = new ArrayList<>();
                        this.acharPuPv(listaPuPv, paresEstados.get(chave).getQu(), paresEstados.get(chave).getQv(),
                                sigma.get(k), arquivo);
                        // listaPuPv = tem o PuPv; sigma.get(k) = simbolo; paresEstados.get(chave).getQu() = qu paresEstados.get(chave).getQv() = qv;
                    }
                }
            
 
            }
            
            System.out.println();
        }
    }

    public void acharPuPv(List<State> listaPuPv, State qu, State qv, String simbolo, Arquivo arquivo) {
        System.out.println("Simbolo: " + simbolo);
        List<Transition> listaTransitions = arquivo.listaTransicoes(arquivo.getDoc());
        List<State> listaEstStates = arquivo.listaEstados(arquivo.getDoc());
        boolean quQvVisitados = false;
        for (Transition transition : listaTransitions) {
            if (transition.getFrom() == qu.getId()) {
                if (transition.getRead().equals(simbolo)) {
                    listaPuPv.add(listaEstStates.get(transition.getTo()));
                    if(quQvVisitados){
                        return;
                    }else{
                        quQvVisitados = true;
                    }
                }
            }
            if (transition.getFrom() == qv.getId()) {
                if (transition.getRead().equals(simbolo)) {
                    listaPuPv.add(listaEstStates.get(transition.getTo()));
                }
                if(quQvVisitados){
                    return;
                }else{
                    quQvVisitados = true;
                }
            }
        }

    }

}
