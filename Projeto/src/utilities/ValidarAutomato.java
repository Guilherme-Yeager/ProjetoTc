package utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ValidarAutomato {

    public boolean isAfd(Document doc) {
        try {
            NodeList listaTransicoes = doc.getElementsByTagName("transition");
            String from = "", to = "", read = "";
            List<Transition> transicoesInfo = new ArrayList<>();
            for (int i = 0; i < listaTransicoes.getLength(); i++) {
                Node noTrans = listaTransicoes.item(i);
                NodeList filhosNoTrans = noTrans.getChildNodes();
                for (int j = 0; j < filhosNoTrans.getLength(); j++) {
                    Node noFilho = filhosNoTrans.item(j);
                    if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
                        if (noFilho.getNodeName() == "from") {
                            from = noFilho.getTextContent();
                        } else if (noFilho.getNodeName() == "to") {
                            to = noFilho.getTextContent();
                        } else {
                            read = noFilho.getTextContent();
                        }
                    }
                }
                Transition transition = new Transition(i, from, to, read);
                transicoesInfo.add(transition);
            }
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
            NodeList listaTransicoes = doc.getElementsByTagName("transition");
            String from = "", to = "", read = "";
            List<Transition> transicoesInfo = new ArrayList<>();
            for (int i = 0; i < listaTransicoes.getLength(); i++) {
                Node noTrans = listaTransicoes.item(i);
                NodeList filhosNoTrans = noTrans.getChildNodes();
                for (int j = 0; j < filhosNoTrans.getLength(); j++) {
                    Node noFilho = filhosNoTrans.item(j);
                    if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
                        if (noFilho.getNodeName() == "from") {
                            from = noFilho.getTextContent();
                        } else if (noFilho.getNodeName() == "to") {
                            to = noFilho.getTextContent();
                        } else {
                            read = noFilho.getTextContent();
                        }
                    }
                }
                Transition transition = new Transition(i, from, to, read);
                transicoesInfo.add(transition);
            }
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

}
