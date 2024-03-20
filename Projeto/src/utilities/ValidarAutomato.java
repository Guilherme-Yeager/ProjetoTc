package utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;

public class ValidarAutomato {

    public boolean isAfd(Document doc) {
        try {
            List<Transition> transicoesInfo = utilities.Arquivo.listaTransicoes(doc);
            List<State> estados = utilities.Arquivo.listaEstados(doc);
            if(estados.isEmpty()){
                return false;
            }
            for (int i = 0; i < transicoesInfo.size(); i++) {
                Transition noTransition = transicoesInfo.get(i);
                if (noTransition.getRead().isEmpty()) {
                    return false;
                }
                for (int j = i + 1; j < transicoesInfo.size(); j++) {
                    if (Integer.toString(noTransition.getFrom())
                            .equals(Integer.toString(transicoesInfo.get(j).getFrom()))) {
                        if (noTransition.getRead().equals(transicoesInfo.get(j).getRead())) {
                            return false;

                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isComplete(Document doc) {
        JOptionPane.showMessageDialog(null, "No próximo passo informe os símbolos do alfabeto.",
                "Informe o Sigma:", JOptionPane.INFORMATION_MESSAGE);
        Set<String> sigma = new HashSet<>();
        String simbolo;
        do {
            simbolo = JOptionPane.showInputDialog(null, "Informe um símbolo do alfabeto:", "Informe o Sigma:",
                    JOptionPane.PLAIN_MESSAGE);
            if (simbolo != null && !simbolo.isEmpty()) {
                sigma.add(simbolo);
            } else {
                if (simbolo == null) {
                    break;
                }
            }
        } while (true);
        try {
            List<Transition> transicoesInfo = utilities.Arquivo.listaTransicoes(doc);
            String fromAnalisados = "";
            Set<String> sigmaDoAutomato = new HashSet<>();
            for (int i = 0; i < transicoesInfo.size(); i++) {
                Transition noTransition = transicoesInfo.get(i);
                if (fromAnalisados.contains(Integer.toString(noTransition.getFrom()))) {
                    continue;
                }
                fromAnalisados += noTransition.getFrom();
                sigmaDoAutomato.add(noTransition.getRead());
                for (int j = i + 1; j < transicoesInfo.size(); j++) {
                    if (Integer.toString(noTransition.getFrom())
                            .equals(Integer.toString(transicoesInfo.get(j).getFrom()))) {
                        sigmaDoAutomato.add(transicoesInfo.get(j).getRead());
                    }
                }
                if (!sigma.equals(sigmaDoAutomato)) {
                    return false;
                }
                sigmaDoAutomato.clear();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean accessibleStates(Document doc) {
        List<State> estados = utilities.Arquivo.listaEstados(doc);
        List<Transition> transicoes = utilities.Arquivo.listaTransicoes(doc);
        State estadoInicial = null;
        for (State estado : estados) {
            if (estado.getIsInitial()) {
                estadoInicial = estado;
            }
        }
        if (estadoInicial == null) {
            return false;
        }
        List<State> estadosAlcancados = new ArrayList<>();
        verificarEstadosAlcancados(estados, estadosAlcancados, estadoInicial, transicoes);
        Set<State> conjEstadosAlcancados = new HashSet<>(estadosAlcancados);
        Set<State> conjEstados = new HashSet<>(estados);
        return conjEstadosAlcancados.equals(conjEstados);
    }

    public static void verificarEstadosAlcancados(List<State> estados, List<State> estadosAlcancados, State estadoAtual,
            List<Transition> transicoes) {
        estadosAlcancados.add(estadoAtual);
        for (Transition transicao : transicoes) {
            if (transicao.getFrom() == estadoAtual.getId()) {
                State proximoEstado = estados.get(transicao.getTo());
                if (!estadosAlcancados.contains(proximoEstado)) {
                    verificarEstadosAlcancados(estados, estadosAlcancados, proximoEstado, transicoes);
                }
            }
        }
    }

}
