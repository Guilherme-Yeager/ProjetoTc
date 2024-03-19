import utilities.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Arquivo arquivo = new Arquivo();
        String caminho = arquivo.obterCaminho();
        if (caminho != null) {
            ValidarAutomato validarAutomato = new ValidarAutomato();
            if (validarAutomato.isAfd(arquivo.getDoc())) {
                System.out.println("É um afd");
                if (validarAutomato.isComplete(arquivo.getDoc())) {
                    System.out.println("É completo");
                    if (validarAutomato.accessibleStates(arquivo.getDoc())) {
                        System.out.println("Todos Estados São Acessíveis");
                    } else {
                        System.out.println("Nem Todos Estados Não são Acessíveis");
                    }
                } else {
                    System.out.println("Não É completo");
                }
            }
        }
        // Finalizar funcionalidade
    }
}
