import java.io.IOException;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) throws IOException {

    try{
        Automato automato1 = Automato.importarAutomato(Automato.obterCaminho()); 

        Automato newAutomato = new Automato();

        newAutomato.aplicarEstrela(automato1);

        Automato.exportarAutomato(newAutomato, "automato-estrela.jff");
    }catch(NullPointerException e){
        JOptionPane.showMessageDialog(null,"Arquivo vazio/não encontrado\nOperação cancelada");
    }
}
}