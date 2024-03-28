import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class Automato implements OperacoesAutomatoInterface {
    public List<Estado> estados;
    public List<Transicao> transicoes;

    public Automato() {
        this.estados = new ArrayList<Estado>();
        this.transicoes = new ArrayList<Transicao>();
    }

    @Override
    public void aplicarConcatenacao(Automato automato1, Automato automato2) {
        Automato novoAutomato = new Automato();

        List<Integer> estadosFinais = new ArrayList<Integer>();

        // percorre a lista de estados do automato1 modificando os IDS e adiciona os
        // estados ao novo automato
        for (Estado estado : automato1.estados) {
            novoAutomato.estados.add(estado);
        }

        // percorre a lista de estados do automato 2 modificando os IDS e adiciona os
        // estados ao novo automato
        for (Estado estado : automato2.estados) {
            estado.id = Integer.toString((Integer.parseInt(estado.id)) + automato1.estados.size());
            estado.name = "q" + estado.id;
            estado.y = estado.y + 200;
            novoAutomato.estados.add(estado);
        }

        // percorre a lista de transições do automato 1 aplicando as alterações de ids
        for (Transicao transicao : automato1.transicoes) {
            novoAutomato.transicoes.add(transicao);
        }

        // percorre a lista de transições do automato 2 aplicando as alterações de ids
        for (Transicao transicao : automato2.transicoes) {
            transicao.from = transicao.from + automato1.estados.size();
            transicao.to = transicao.to + automato1.estados.size();
            novoAutomato.transicoes.add(transicao);
        }

        for (Estado estado : automato1.estados) {
            // retira a tag de estado final dos estados do automato 1 e salva na lista
            // finalState
            if (estado.isFinal) {
                estadosFinais.add(Integer.parseInt(estado.id));
                estado.isFinal = false;
            }
        }

        for (Estado estado : automato2.estados) {
            if (estado.isInitial) {
                // para cada estado final do automato 1, cria uma transição para o estado
                // inicial do automato 2
                for (Integer estadoFinal : estadosFinais) {
                    Transicao transicao = new Transicao();

                    transicao.from = estadoFinal;
                    transicao.to = Integer.parseInt(estado.id);
                    transicao.read = "";

                    novoAutomato.transicoes.add(transicao);
                }
                estado.isInitial = false;
            }
        }

        this.estados = novoAutomato.estados;
        this.transicoes = novoAutomato.transicoes;

        JOptionPane.showMessageDialog(null,"Autômato concatenado com sucesso!\nArquivo: automato-concatenacao.jff");
    }
}

    