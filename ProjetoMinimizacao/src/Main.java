import utilities.*;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) throws Exception {
        Arquivo arquivo = new Arquivo();
        String caminho = arquivo.obterCaminho(), aviso = "";
        if (caminho != null) {
            arquivo.listaEstados(arquivo.getDoc());
            arquivo.listaTransicoes(arquivo.getDoc());
            ValidarAutomato validarAutomato = new ValidarAutomato(arquivo);
            if (validarAutomato.isAfd(arquivo.getDoc())) {
                if (validarAutomato.isComplete(arquivo.getDoc())) {
                    if (validarAutomato.accessibleStates(arquivo.getDoc())) {
                        Minimization minimizacao = new Minimization();
                        minimizacao.minimizarAutomato(arquivo, validarAutomato);
                    } else {
                        aviso = "O autômato não possui todos os estados acessíveis.";
                    }
                } else {
                    aviso = "O autômato não é completo.";
                }
            } else {
                aviso = "O autômato não é um AFD.";
            }
        }
        if(caminho == null){
            JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado.", "Informação:", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(!aviso.isEmpty()){
            JOptionPane.showMessageDialog(null, aviso, "Informação:", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
