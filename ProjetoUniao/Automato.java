import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Automato implements OperacoesAutomatoInterface {
    public List<Estado> estados;
    public List<Transicao> transicoes;
    static boolean flag = false;

    public Automato() {
        this.estados = new ArrayList<Estado>();
        this.transicoes = new ArrayList<Transicao>();
    }

    @Override
    public void aplicarUniao(Automato automato1, Automato automato2) {
        int spaceX = 300, spaceY = 200;
        Automato novoAutomato = new Automato();
    
        // Adiciona todos os estados do automato1 ao novo automato
        for (Estado estado : automato1.estados) {
            novoAutomato.estados.add(estado);
            estado.x = estado.x + spaceX;
        }
    
        // Adiciona todos os estados do automato2 ao novo automato, ajustando seus IDs e posições
        for (Estado estado : automato2.estados) {
            estado.id = Integer.toString((Integer.parseInt(estado.id)) + automato1.estados.size());
            estado.name = "q" + estado.id;
            estado.x = estado.x + spaceX - 150;
            estado.y = estado.y + spaceY + 100;
            novoAutomato.estados.add(estado);
        }
    
        // Adiciona todas as transicoes do automato1 ao novo automato
        for (Transicao transicao : automato1.transicoes) {
            novoAutomato.transicoes.add(transicao);
        }
    
        // Adiciona todas as transicoes do automato2 ao novo automato, ajustando as referencias de estados
        for (Transicao transicao : automato2.transicoes) {
            transicao.from = transicao.from + automato1.estados.size();
            transicao.to = transicao.to + automato1.estados.size();
            novoAutomato.transicoes.add(transicao);
        }
    
        // Adiciona um novo estado inicial ao novo automato
        Estado novoEstadoInicial = new Estado();
        novoEstadoInicial.id = Integer.toString(novoAutomato.estados.size());
        novoEstadoInicial.name = "q" + novoEstadoInicial.id;
        novoEstadoInicial.x = 60 + spaceX;
        novoEstadoInicial.y = 85 + spaceY;
        novoEstadoInicial.isInitial = true;
        novoAutomato.estados.add(novoEstadoInicial);

        // Adiciona um novo estado final ao novo automato
        Estado novoEstadoFinal = new Estado();
    
        // Adiciona transicoes lambda do novo estado inicial para os estados iniciais dos automatos originais
        for (Estado estado : automato1.estados) {
            if (estado.isInitial) {
                Transicao transicao = new Transicao();
                transicao.from = Integer.parseInt(novoEstadoInicial.id);
                transicao.to = Integer.parseInt(estado.id);
                transicao.read = "";
                novoAutomato.transicoes.add(transicao);
                // Verifica se o estado do automato1 além de inicial também é final, fazendo todos os 
                //estados finais de ambos os autômatos se ligarem a um novo estado final
                if(estado.isFinal){
                    if(novoEstadoFinal.id == null){
                        novoEstadoFinal.id = Integer.toString(novoAutomato.estados.size());
                        novoEstadoFinal.name = "q" + novoEstadoFinal.id;
                        novoEstadoFinal.x = 220 + spaceX;
                        novoEstadoFinal.y = 80 + spaceY;
                        novoEstadoFinal.isFinal = true;
                        novoAutomato.estados.add(novoEstadoFinal);
                    }
                    Transicao transicao2 = new Transicao();
                    transicao2.from = Integer.parseInt(estado.id);
                    transicao2.to = Integer.parseInt(novoEstadoFinal.id);
                    transicao2.read = "";
                    novoAutomato.transicoes.add(transicao2);
                    for(Estado estado2 : automato1.estados){
                        if(estado2.isFinal){
                            Transicao transicao3 = new Transicao();
                            transicao3.from = Integer.parseInt(estado2.id);
                            transicao3.to = Integer.parseInt(novoEstadoFinal.id);
                            transicao3.read = "";
                            novoAutomato.transicoes.add(transicao3);
                        }
                    }
                    for(Estado estado2 : automato2.estados){
                        if(estado2.isFinal){
                            Transicao transicao3 = new Transicao();
                            transicao3.from = Integer.parseInt(estado2.id);
                            transicao3.to = Integer.parseInt(novoEstadoFinal.id);
                            transicao3.read = "";
                            novoAutomato.transicoes.add(transicao3);
                        }
                    }
                }
                estado.isInitial = false;
            }
        }
    
        // Adiciona transicoes lambda do novo estado inicial para os estados iniciais dos automatos originais
        for (Estado estado : automato2.estados) {
            if (estado.isInitial) {
                Transicao transicao = new Transicao();
                transicao.from = Integer.parseInt(novoEstadoInicial.id);
                transicao.to = Integer.parseInt(estado.id);
                transicao.read = "";
                novoAutomato.transicoes.add(transicao);
                // Verifica se o estado do automato2 além de inicial também é final, fazendo todos os 
                //estados finais de ambos os autômatos se ligarem a um novo estado final
                if(estado.isFinal){
                    //Verifica se o ID do novo estado final foi criado anteriormente
                    if(novoEstadoFinal.id==null){
                        novoEstadoFinal.id = Integer.toString(novoAutomato.estados.size());
                        novoEstadoFinal.name = "q" + novoEstadoFinal.id;
                        novoEstadoFinal.x = 220 + spaceX;
                        novoEstadoFinal.y = 80 + spaceY;
                        novoEstadoFinal.isFinal = true;
                        novoAutomato.estados.add(novoEstadoFinal);
                    }
                    Transicao transicao2 = new Transicao();
                    transicao2.from = Integer.parseInt(estado.id);
                    transicao2.to = Integer.parseInt(novoEstadoFinal.id);
                    transicao2.read = "";
                    novoAutomato.transicoes.add(transicao2);
                    for(Estado estado2 : automato2.estados){
                        if(estado2.isFinal){
                            Transicao transicao3 = new Transicao();
                            transicao3.from = Integer.parseInt(estado2.id);
                            transicao3.to = Integer.parseInt(novoEstadoFinal.id);
                            transicao3.read = "";
                            novoAutomato.transicoes.add(transicao3);
                        }
                    }
                    for(Estado estado2 : automato1.estados){
                        if(estado2.isFinal){
                            Transicao transicao3 = new Transicao();
                            transicao3.from = Integer.parseInt(estado2.id);
                            transicao3.to = Integer.parseInt(novoEstadoFinal.id);
                            transicao3.read = "";
                            novoAutomato.transicoes.add(transicao3);
                        }
                    }
                }
                estado.isInitial = false;
            }
        }
        this.estados = novoAutomato.estados;
        this.transicoes = novoAutomato.transicoes;
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
            if (transicao.read == null || transicao.read.isEmpty()) {
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

}