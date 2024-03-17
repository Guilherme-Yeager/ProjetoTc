
import file.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Arquivo arquivo = new Arquivo();
        arquivo.obterCaminho();
        ValidarAutomato validarAutomato = new ValidarAutomato();
        if (validarAutomato.isAfd(arquivo)) {
            System.out.println("é um AFD");
        } else {
            System.out.println("Não é um AFD");
        }
    }
}
