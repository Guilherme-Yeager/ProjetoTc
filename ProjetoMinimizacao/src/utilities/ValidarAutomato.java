package utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;

public class ValidarAutomato {
    private Set<String> sigma;
    private Arquivo arquivo;

    public ValidarAutomato(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public Set<String> getSigma() {
        return sigma;
    }

    public void setSigma(Set<String> sigma) {
        this.sigma = sigma;
    }

    public boolean isAfd(Document doc) {
        try {
            List<Transition> transicoesInfo = this.getArquivo().listaTransicoes(doc);
            List<State> estados = this.getArquivo().listaEstados(doc);
            boolean temInicial = false;
            for (State s : estados) {
                if (s.getIsInitial()) {
                    temInicial = true;
                }
            }
            if (!temInicial) {
                JOptionPane.showMessageDialog(null, "Autômato não possui estado inicial", "Informação:",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            if (estados.isEmpty()) {
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

    public boolean isComplete() {
        Set<String> sigma = this.getSigma();
        try {
            List<Transition> transicoesInfo = this.getArquivo().getListaTransicoes();
            List<State> estados = this.getArquivo().getListaEstados();
            Set<String> sigmaDoEstado = new HashSet<>();
            for (State state : estados) {
                for (int i = 0; i < transicoesInfo.size(); i++) {
                    if (state.getId() == transicoesInfo.get(i).getFrom()) {
                        sigmaDoEstado.add(transicoesInfo.get(i).getRead());
                    }
                }
                if ((!sigma.isEmpty() && sigmaDoEstado.isEmpty()) || !sigma.equals(sigmaDoEstado)) {
                    return false;
                }
                sigmaDoEstado.clear();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isComplete(Document doc) {
        Set<String> sigma = new HashSet<>();
        try {
            List<Transition> transicoesInfo = this.getArquivo().listaTransicoes(doc);
            List<State> estados = this.getArquivo().getListaEstados();
            for (State state : estados) {
                for (int i = 0; i < transicoesInfo.size(); i++) {
                    if (state.getId() == transicoesInfo.get(i).getFrom()) {
                        sigma.add(transicoesInfo.get(i).getRead());
                    }
                }
            }
            this.setSigma(sigma);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean accessibleStates() {
        List<State> estados = this.getArquivo().getListaEstados();
        List<Transition> transicoes = this.getArquivo().getListaTransicoes();
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

    public boolean accessibleStates(Document doc) {
        List<State> estados = this.getArquivo().getListaEstados();
        List<Transition> transicoes = this.getArquivo().getListaTransicoes();
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
        for (int i = 0; i < this.getArquivo().getListaEstados().size(); i++) {
            if (!estadosAlcancados.contains(this.getArquivo().getListaEstados().get(i))) {
                this.getArquivo().getListaEstados().remove(i);
                i--;
            }

        }
        List<Integer> ids = new ArrayList<>();
        for (State estado : estadosAlcancados) {
            ids.add(estado.getId());
        }
        for (int i = 0; i < transicoes.size(); i++) {
            if (!ids.contains(transicoes.get(i).getFrom()) || !ids.contains(transicoes.get(i).getTo())) {
                transicoes.remove(i);
                i--;
            }
        }
        this.getArquivo().ajustarEstados(this.getArquivo().getListaEstados());
        this.getArquivo().ajustarTransicoes(this.getArquivo().getListaTransicoes());
        return true;
    }

    public void verificarEstadosAlcancados(List<State> estados, List<State> estadosAlcancados, State estadoAtual,
            List<Transition> transicoes) {
        estadosAlcancados.add(estadoAtual);
        for (Transition transicao : transicoes) {
            if (transicao.getFrom() == estadoAtual.getId()) {
                State proximoEstado = estados.get(transicao.getTo());
                if (!estadosAlcancados.contains(proximoEstado)) {
                    this.verificarEstadosAlcancados(estados, estadosAlcancados, proximoEstado, transicoes);
                }
            }
        }
    }

    public void tornarTotal() {
        if (this.getSigma().isEmpty()) {
            return;
        }
        State estado = new State(this.getArquivo().getListaEstados().size(),
                "q" + Integer.toString(this.getArquivo().getListaEstados().size()), false, false, 400, 200, "");
        this.getArquivo().getListaEstados().add(estado);
        List<Transition> transicoesInfo = new ArrayList<>();
        Set<String> sigmaDoEstado = new HashSet<>();
        for (int i = 0; i < this.getArquivo().getListaEstados().size(); i++) {
            for (Transition transicao : this.getArquivo().getListaTransicoes()) {
                if (transicao.getFrom() == i) {
                    sigmaDoEstado.add(transicao.getRead());
                }
            }
            if (!sigmaDoEstado.equals(this.getSigma())) {
                Set<String> diferenca = new HashSet<>(sigma);
                diferenca.removeAll(sigmaDoEstado);
                for (String simbolo : diferenca) {
                    transicoesInfo.add(new Transition(i, estado.getId(), simbolo));
                }
            }
            sigmaDoEstado.clear();
        }
        this.getArquivo().getListaTransicoes().addAll(transicoesInfo);
    }

}
