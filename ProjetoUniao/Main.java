import java.io.IOException;
import javax.swing.JOptionPane;

public class Main{
    public static void main(String[] args) throws IOException {
        try{
            Automato automato1 = Automato.importarAutomato(Automato.obterCaminho());
    
            Automato automato2 = Automato.importarAutomato(Automato.obterCaminho());  
    
            Automato newAutomato = new Automato();
    
            newAutomato.aplicarUniao(automato1, automato2);
    
            Automato.exportarAutomato(newAutomato, "automato-uniao.jff");
            
            JOptionPane.showMessageDialog(null, "Operação concluída com sucesso.");
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(null,"Arquivo vazio/não encontrado\nOperação cancelada");
        }
        
    }

}