package utilities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;

public class ValidarAutomato {

    public boolean isAfd(Document doc) {
        try {
            List<Transition> transicoesInfo = utilities.Arquivo.listaTransicoes(doc);
            for (int i = 0; i < transicoesInfo.size(); i++) {
                Transition noTransition = transicoesInfo.get(i);
                if (noTransition.getRead().isEmpty()) {
                    return false;
                }
                for (int j = i + 1; j < transicoesInfo.size(); j++) {
                    if (noTransition.getFrom().equals(transicoesInfo.get(j).getFrom())) {
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
            if (simbolo != null) {
                sigma.add(simbolo);
            } else {
                break;
            }
        } while (true);
        try {
            List<Transition> transicoesInfo = utilities.Arquivo.listaTransicoes(doc);
            String fromAnalisados = "";
            Set<String> sigmaDoAutomato = new HashSet<>();
            for (int i = 0; i < transicoesInfo.size(); i++) {
                Transition noTransition = transicoesInfo.get(i);
                if (fromAnalisados.contains(noTransition.getFrom())) {
                    continue;
                }
                fromAnalisados += noTransition.getFrom();
                sigmaDoAutomato.add(noTransition.getRead());
                for (int j = i + 1; j < transicoesInfo.size(); j++) {
                    if (noTransition.getFrom().equals(transicoesInfo.get(j).getFrom())) {
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
        // TODO
        return true;
    }

}
