import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Arquivos {

    public static final String obterCaminho = null;
    static boolean flag = false;
    public static String obterCaminho() {
        
        try {
            FileNameExtensionFilter arqFiltro = new FileNameExtensionFilter("Somente arquivos .jff", "jff");
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.addChoosableFileFilter(arqFiltro);
            if(!flag){
                jFileChooser.setDialogTitle("Selecione o primeiro arquivo .jff");
                flag = true;
            }else{
                jFileChooser.setDialogTitle("Selecione o segundo arquivo .jff");
            }
            
            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File arquivo = jFileChooser.getSelectedFile();
                String caminho = arquivo.getAbsolutePath();
                return caminho;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }
    
    
public static Automato importarAutomato(String diretorio) throws IOException {

        File arquivo = new File(diretorio);

        Automato automato = new Automato();
        automato.estados = new ArrayList<>();
        automato.transicoes = new ArrayList<>();

        Scanner scanner = new Scanner(arquivo);
        
    while (scanner.hasNextLine()) {
        String linha = scanner.nextLine();

        // Quando encontra Inicio de Estado
        if (linha.contains("\t\t<state ")) {
            Estado estado = new Estado();
            int inicioId = linha.indexOf("=\"") + 2;
            int fimId = linha.indexOf("\" ");
            String id = linha.substring(inicioId, fimId);

            estado.id = id;

            linha = linha.substring(fimId + 2);

            int inicioName = linha.indexOf("=\"") + 2;
            int fimName = linha.indexOf("\">");
            String name = linha.substring(inicioName, fimName);

            estado.name = name;

            linha = scanner.nextLine();

            int inicioX = linha.indexOf(">") + 1;
            int fimX = linha.indexOf("</");
            String x = linha.substring(inicioX, fimX);

            estado.x = Float.parseFloat(x);

            linha = scanner.nextLine();

            int inicioY = linha.indexOf(">") + 1;
            int fimY = linha.indexOf("</");
            String y = linha.substring(inicioY, fimY);

            estado.y = Float.parseFloat(y);

            // Se chegou no final do estado
            while (!linha.contains("\t\t</state>")) {
                // Se é estado inicial
                if (linha.contains("<initial/>")) {
                    estado.isInitial = true;
                }
                // Se é estado final
                if (linha.contains("<final/>")) {
                    estado.isFinal = true;
                }
                linha = scanner.nextLine();
            }

            automato.estados.add(estado);

        } else if (linha.contains("\t\t<transition")) {
            Transicao transicao = new Transicao();

            linha = scanner.nextLine();

            // From
            int inicioFrom = linha.indexOf(">") + 1;
            int fimFrom = linha.indexOf("</");
            String from = linha.substring(inicioFrom, fimFrom);

            transicao.from = Integer.parseInt(from);

            linha = scanner.nextLine();

            // To
            int inicioTo = linha.indexOf(">") + 1;
            int fimTo = linha.indexOf("</");
            String to = linha.substring(inicioTo, fimTo);

            transicao.to = Integer.parseInt(to);

            linha = scanner.nextLine();

            // Read
            if (linha.contains("<read/>")) {
                transicao.read = "";
            } else {
                int inicioRead = linha.indexOf(">") + 1;
                int fimRead = linha.indexOf("</");
                String read = linha.substring(inicioRead, fimRead);

                transicao.read = read;
            }

            automato.transicoes.add(transicao);

            linha = scanner.nextLine();
        }

    }
    scanner.close();
    return automato;
}

    public static void exportarAutomato(Automato automato, String diretorio) throws IOException {
        File arquivo = new File(diretorio);
        FileWriter writer = new FileWriter(arquivo);

        writer.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Created with JFLAP 6.4. By Safi--><structure>&#13;\n");
        writer.write("\t<type>fa</type>&#13;\n");
        writer.write("\t<automaton>&#13;\n");
        writer.write("\t\t<!--The list of states.-->&#13;\n");

        // Escreve os estados
        for (Estado estado : automato.estados) {
            writer.write("\t\t<state id=\"" + estado.id + "\" name=\"" + estado.name + "\">&#13;\n");
            writer.write("\t\t\t<x>" + estado.x + "</x>&#13;\n");
            writer.write("\t\t\t<y>" + estado.y + "</y>&#13;\n");
            if (estado.isInitial) {
                writer.write("\t\t\t<initial/>&#13;\n");
            }
            if (estado.isFinal) {
                writer.write("\t\t\t<final/>&#13;\n");
            }
            writer.write("\t\t</state>&#13;\n");
        }

        writer.write("\t\t<!--The list of transitions.-->&#13;\n");

        // Escreve as transições
        for (Transicao transicao : automato.transicoes) {
            writer.write("\t\t<transition>&#13;\n");
            writer.write("\t\t\t<from>" + transicao.from + "</from>&#13;\n");
            writer.write("\t\t\t<to>" + transicao.to + "</to>&#13;\n");
            // Caso a transição seja epsilon
            if (transicao.read.isBlank()) {
                writer.write("\t\t\t<read/>\n");
            } else {
                writer.write("\t\t\t<read>" + transicao.read + "</read>&#13;\n");
            }
            writer.write("\t\t</transition>&#13;\n");
        }

        writer.write("\t</automaton>&#13;\n");
        writer.write("</structure>");
        writer.close();
    }
}
