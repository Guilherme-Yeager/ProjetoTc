package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class Arquivo {
    private Document doc;

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
                setDoc(this.lerArquivo(caminho));
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
    public List<Transition> listaTransicoes(Document doc) {
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
    public List<State> listaEstados(Document doc) {
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
                    } 
                }
            }
            State state = new State(id, name, isInitial, isFinal, x, y, "");
            listaEstadosInfo.add(state);
            isInitial = false;
            isFinal = false;
        }
        return listaEstadosInfo;
    }

    public void gravarAutomato(List<State> novosEstados, List<Transition> novasTransicoes) {
        JFileChooser jFileChooser = new JFileChooser();
        if (jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File automatoMinimizado = jFileChooser.getSelectedFile();
            automatoMinimizado = new File(automatoMinimizado.getAbsoluteFile() + ".jff");

            try {
                String arquivo = automatoMinimizado.getAbsolutePath();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder dc = dbf.newDocumentBuilder();
                Document d = dc.newDocument();

                Element raiz = d.createElement("structure");
                d.appendChild(raiz);

                Element type = d.createElement("type");
                type.appendChild(d.createTextNode("fa"));
                raiz.appendChild(type);

                Element automato = d.createElement("automaton");
                raiz.appendChild(automato);
                for (State estado : novosEstados) {
                    Element state = d.createElement("state");
                    Attr attrId = d.createAttribute("id");
                    Attr attrName = d.createAttribute("name");
                    attrId.setValue(Integer.toString(estado.getId()));
                    attrName.setValue("q" + Integer.toString(estado.getId()));
                    state.setAttributeNode(attrId);
                    state.setAttributeNode(attrName);
                    Element x = d.createElement("x");
                    Element y = d.createElement("y");
                    Element isFinal = d.createElement("final");
                    Element inicial = d.createElement("initial");
                    Element label = d.createElement("label");
                    x.appendChild(d.createTextNode(Float.toString(estado.getX())));
                    y.appendChild(d.createTextNode(Float.toString(estado.getY())));
                    state.appendChild(x);
                    state.appendChild(y);
                    if (estado.getIsInitial()) {
                        state.appendChild(inicial);
                    }
                    if (estado.getIsFinal()) {
                        state.appendChild(isFinal);
                    }
                    if (!estado.getLabel().isEmpty()) {
                        label.appendChild(d.createTextNode(estado.getLabel()));
                        state.appendChild(label);
                    }
                    automato.appendChild(state);
                }
                for (Transition transicao : novasTransicoes) {
                    Element transition = d.createElement("transition");
                    Element from = d.createElement("from");
                    Element to = d.createElement("to");
                    Element read = d.createElement("read");
                    from.appendChild(d.createTextNode(Integer.toString(transicao.getFrom())));
                    to.appendChild(d.createTextNode(Integer.toString(transicao.getTo())));
                    read.appendChild(d.createTextNode(transicao.getRead()));
                    transition.appendChild(from);
                    transition.appendChild(to);
                    transition.appendChild(read);
                    automato.appendChild(transition);
                }

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer t = tf.newTransformer();
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource domSource = new DOMSource(d);
                StreamResult streamResult = new StreamResult(new File(arquivo));
                t.transform(domSource, streamResult);
                
                System.out.println("Arquivo salvo com sucesso!");
                System.gc();
                
                
            } catch (ParserConfigurationException | TransformerException e) {
                System.err.println("Erro durante a configuração ou transformação do documento XML: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
