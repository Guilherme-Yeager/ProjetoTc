package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Arquivo {
    private String caminho;

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public static String obterCaminho() {
        try {
            FileNameExtensionFilter arqFiltro = new FileNameExtensionFilter("Somentearquivos .jff", ".jff");
            JFileChooser escolhe = new JFileChooser();
            escolhe.setAcceptAllFileFilterUsed(true);
            escolhe.addChoosableFileFilter(arqFiltro);
            escolhe.setDialogTitle("Selecione um arquivo .jff");
            if (escolhe.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File arquivo = escolhe.getSelectedFile();
                String caminho = arquivo.getAbsolutePath();
                return caminho;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public static void exportarAutomato(Automato automato) throws IOException {
    	JFileChooser escolhe = new JFileChooser();
    	escolhe.setDialogTitle("Salve o arquivo .jff");
    	if (escolhe.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
    		File arquivo = escolhe.getSelectedFile();
    		arquivo = new File(arquivo.getAbsoluteFile() + ".jff");

    		FileWriter writer = new FileWriter(arquivo);

    		writer.write(
    				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><structure>&#13;\n");
    		writer.write("\t<type>fa</type>&#13;\n");
    		writer.write("\t<automaton>&#13;\n");
    		writer.write("\t\t<!--The list of states.-->&#13;\n");
    		// Escreve os estados
    		for (Estado estado : automato.getEstados()) {
    			writer.write("\t\t<state id=\"" + estado.getId() + "\" name=\"" + estado.getName() + "\">&#13;\n");
    			writer.write("\t\t\t<x>" + estado.getX() + "</x>&#13;\n");
    			writer.write("\t\t\t<y>" + estado.getY() + "</y>&#13;\n");
    			if (estado.isInitial()) {
    				writer.write("\t\t\t<initial/>&#13;\n");
    			}
    			if (estado.isFinal()) {
    				writer.write("\t\t\t<final/>&#13;\n");
    			}
    			if (estado.getLabel().size() > 0) {

    				writer.write("\t\t\t<label>" + Automato.retornarLabelComVirgula(estado.getLabel()) + "</label>&#13;\n");
    			}
    			writer.write("\t\t</state>&#13;\n");
    		}

    		writer.write("\t\t<!--The list of transitions.-->&#13;\n");

    		// Escreve as transições
    		for (Transicao transicao : automato.getTransicoes()) {
    			writer.write("\t\t<transition>&#13;\n");
    			writer.write("\t\t\t<from>" + transicao.getFrom() + "</from>&#13;\n");
    			writer.write("\t\t\t<to>" + transicao.getTo() + "</to>&#13;\n");
    			// Caso a transição seja epsilon
    			if (transicao.getRead().isBlank()) {
    				writer.write("\t\t\t<read/>\n");
    			} else {
    				writer.write("\t\t\t<read>" + transicao.getRead() + "</read>&#13;\n");
    			}
    			writer.write("\t\t</transition>&#13;\n");
    		}

    		writer.write("\t</automaton>&#13;\n");
    		writer.write("</structure>");
    		writer.close();
    	}
    }

    public static Automato carregaArquivo(String caminho) {
        try {
            File arquivoEntrada = new File(caminho);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(arquivoEntrada);
            doc.getDocumentElement().normalize();

            Automato automato = new Automato();

            NodeList listaEstados = doc.getElementsByTagName("state");
            for (int i = 0; i < listaEstados.getLength(); i++) {
                Node estadoNo = listaEstados.item(i);
                if (estadoNo.getNodeType() == Node.ELEMENT_NODE) {
                    Element estadoElemento = (Element) estadoNo;
                    String id = estadoElemento.getAttribute("id");
                    String name = estadoElemento.getAttribute("name");
                    double x = Double.parseDouble(estadoElemento.getElementsByTagName("x").item(0).getTextContent());
                    double y = Double.parseDouble(estadoElemento.getElementsByTagName("y").item(0).getTextContent());
                    boolean isInitial = estadoElemento.getElementsByTagName("initial").getLength() > 0;
                    boolean isFinal = estadoElemento.getElementsByTagName("final").getLength() > 0;

                    Estado estado = new Estado(id, name, x, y, isInitial, isFinal);
                    if (isInitial == true) {
                        automato.addEstadoInicial(estado);
                    }
                    automato.addEstado(estado);
                }
            }

            NodeList listaTransicoes = doc.getElementsByTagName("transition");
            for (int j = 0; j < listaTransicoes.getLength(); j++) {
                Node noTransicao = listaTransicoes.item(j);
                if (noTransicao.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoTransicao = (Element) noTransicao;
                    String from = elementoTransicao.getElementsByTagName("from").item(0).getTextContent();
                    String to = elementoTransicao.getElementsByTagName("to").item(0).getTextContent();
                    String read = elementoTransicao.getElementsByTagName("read").item(0).getTextContent();

                    Transicao transicao = new Transicao(from, to, read);
                    automato.addTransicao(transicao);
                }
            }
            return automato;
        } catch (Exception e) {
            System.out.println("Arquivo nulo: " + e.getMessage());
        }
        return null;
    }
}