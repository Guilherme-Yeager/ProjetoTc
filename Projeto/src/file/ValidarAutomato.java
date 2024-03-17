package file;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ValidarAutomato {


    public boolean isAfd(Arquivo arq){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(arq.getCaminho());
            NodeList listaTransicoes = doc.getElementsByTagName("transition"); 
            String from = "", to = "", read = "";
            List<Transition> transicoesInfo = new ArrayList<>();
            for(int i = 0; i < listaTransicoes.getLength(); i++){
                Node noTrans = listaTransicoes.item(i);
                NodeList filhosNoTrans = noTrans.getChildNodes();
                for(int j = 0; j < filhosNoTrans.getLength(); j++){
                    Node noFilho = filhosNoTrans.item(j);
                    if(noFilho.getNodeType() == Node.ELEMENT_NODE){
                        if(noFilho.getNodeName() == "from"){
                            from = noFilho.getTextContent();
                        }else if(noFilho.getNodeName() == "to"){
                            to = noFilho.getTextContent();
                        }else{
                            read = noFilho.getTextContent();
                        }
                    }
                }
                Transition transition = new Transition(i, from, to, read);
                transicoesInfo.add(transition);
            }
            boolean ehAfd = true;
            for (int i = 0; i < transicoesInfo.size(); i++) {
                Transition noTransition = transicoesInfo.get(i);
                for (int j = i + 1; j < transicoesInfo.size(); j++) {
                    if(noTransition.getRead().isEmpty()){
                        ehAfd = false;
                        break;
                    }
                    if(noTransition.getFrom().equals(transicoesInfo.get(j).getFrom())){
                        if(noTransition.getRead().equals(transicoesInfo.get(j).getRead())){
                            ehAfd = false;
                            break;
                        }
                    }
                }
                if(!ehAfd){
                    break;
                }
            }
            return ehAfd;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fdesçafpolçkdsa");
        }
        return false;
    }
}

