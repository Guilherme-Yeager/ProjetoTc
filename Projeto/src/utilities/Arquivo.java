package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Arquivo {
    Document doc;

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    /**
     * Método para o usuário selecionar um arquivo .jff
     * 
     * @return Caminho do arquivo
     */
    public String obterCaminho() {
        try {
            FileNameExtensionFilter arqFiltro = new FileNameExtensionFilter("Somente arquivos .jff", "jff");
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.addChoosableFileFilter(arqFiltro);
            jFileChooser.setDialogTitle("Selecione um arquivo .jff");
            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File arquivo = jFileChooser.getSelectedFile();
                String caminho = "file:///" + arquivo.getAbsolutePath();
                this.setDoc(this.lerArquivo(caminho));
                return caminho;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    /**
     * Método para processar um objeto Document que serve
     * para acessar e manipular um documento XML
     * 
     * @param caminho do arquivo
     * @return Um objeto Document que representa o conteúdo do arquivo
     */
    public Document lerArquivo(String caminho) {
        try {
            if (caminho != null) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(caminho);
                return doc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método para processar o documento para obter
     * a lista de transições do autômato
     * 
     * @param doc é um objeto Document que representa o conteúdo do arquivo
     * @return Uma lista de transições do autômato lido do documento
     */
    public static List<Transition> listaTransicoes(Document doc) {
        NodeList listaTransicoes = doc.getElementsByTagName("transition");
        int from = 0, to = 0;
        String read = "";
        List<Transition> transicoesInfo = new ArrayList<>();
        for (int i = 0; i < listaTransicoes.getLength(); i++) {
            Node noTrans = listaTransicoes.item(i);
            NodeList filhosNoTrans = noTrans.getChildNodes();
            for (int j = 0; j < filhosNoTrans.getLength(); j++) {
                Node noFilho = filhosNoTrans.item(j);
                if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
                    if (noFilho.getNodeName() == "from") {
                        from = Integer.parseInt(noFilho.getTextContent());
                    } else if (noFilho.getNodeName() == "to") {
                        to = Integer.parseInt(noFilho.getTextContent());
                    } else {
                        read = noFilho.getTextContent();
                    }
                }
            }
            Transition transition = new Transition(from, to, read);
            transicoesInfo.add(transition);
        }
        return transicoesInfo;
    }

    /**
     * Método para o processar o documento para obter
     * a lista de estados do autômato
     * 
     * @param doc é um objeto Document que representa o conteúdo do arquivo
     * @return Uma lista de estados do autômato lido do documento
     */
    public static List<State> listaEstados(Document doc) {
        NodeList listaEstados = doc.getElementsByTagName("state");
        String name = "", label = "";
        int id = 0;
        float x = 0.f, y = 0.f;
        boolean isInitial = false, isFinal = false;
        List<State> listaEstadosInfo = new ArrayList<>();
        for (int i = 0; i < listaEstados.getLength(); i++) {
            Node noTrans = listaEstados.item(i);
            NamedNodeMap atributosEstado = noTrans.getAttributes();
            for (int k = 0; k < atributosEstado.getLength(); k++) {
                Node atributo = atributosEstado.item(k);
                if (atributo.getNodeName().equals((String) "id")) {
                    id = Integer.parseInt(atributo.getNodeValue());
                } else {
                    name = atributo.getNodeValue();
                }
            }
            NodeList filhosNoState = noTrans.getChildNodes();
            for (int j = 0; j < filhosNoState.getLength(); j++) {
                Node noFilho = filhosNoState.item(j);
                if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
                    if (noFilho.getNodeName() == "x") {
                        x = Float.parseFloat(noFilho.getTextContent());
                    } else if (noFilho.getNodeName() == "y") {
                        y = Float.parseFloat(noFilho.getTextContent());
                    } else if (noFilho.getNodeName() == "initial") {
                        isInitial = true;
                    } else if (noFilho.getNodeName() == "final") {
                        isFinal = true;
                    } else if (noFilho.getNodeName() == "label") {
                        label = noFilho.getTextContent();
                    }
                }
            }
            State state = new State(id, name, isInitial, isFinal, x, y, label);
            listaEstadosInfo.add(state);
            isInitial = false;
            isFinal = false;
        }
        return listaEstadosInfo;
    }

}
