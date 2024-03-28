import java.io.IOException;
import javax.swing.JOptionPane;
public class Main {
    public static void main(String[] args) throws IOException {

    try{
        Automato automato1 = Arquivos.importarAutomato(Arquivos.obterCaminho());

        Automato automato2 = Arquivos.importarAutomato(Arquivos.obterCaminho());  

        Automato newAutomato = new Automato();

        newAutomato.aplicarConcatenacao(automato1, automato2);

        Arquivos.exportarAutomato(newAutomato, "automato-concatenacao.jff");
    }catch(NullPointerException e){
        JOptionPane.showMessageDialog(null,"Arquivo vazio/não encontrado\nOperação cancelada");
    }
    
    }

}