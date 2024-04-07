import java.io.IOException;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        int opcao = JOptionPane.showOptionDialog(null, "Escolha a abordagem da operação (União):", "Escolha",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { "AFD", "AFN" }, "AFD");

        if (opcao == 0) {
            // Opção AFD selecionada
            try {
                Automato automato1 = Automato.importarAutomato(Automato.obterCaminho());
                Automato automato2 = Automato.importarAutomato(Automato.obterCaminho());
                Automato newAutomato = new Automato();
                newAutomato.aplicarUniaoAFD(automato1, automato2);
                Automato.exportarAutomato(newAutomato, "automato-uniaoAFD.jff");
                JOptionPane.showMessageDialog(null, "Operação concluída com sucesso.");
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Arquivo vazio/não encontrado\nOperação cancelada");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.getMessage();
            }
        } else if (opcao == 1) {
            // Opção AFN selecionada
            try {
                Automato automato1 = Automato.importarAutomato(Automato.obterCaminho());
                Automato automato2 = Automato.importarAutomato(Automato.obterCaminho());
                Automato newAutomato = new Automato();
                newAutomato.aplicarUniaoAFN(automato1, automato2);
                Automato.exportarAutomato(newAutomato, "automato-uniaoAFN.jff");
                JOptionPane.showMessageDialog(null, "Operação concluída com sucesso.");
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Arquivo vazio/não encontrado\nOperação cancelada");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.getMessage();
            }
        }
    }
}