import utilities.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Arquivo arquivo = new Arquivo();
        String caminho = arquivo.obterCaminho();
        if (caminho != null) {
            ValidarAutomato validarAutomato = new ValidarAutomato();
            if (validarAutomato.isAfd(arquivo.getDoc())) {
                if (validarAutomato.isComplete(arquivo.getDoc())) {
                    if (validarAutomato.accessibleStates(arquivo.getDoc())) {
                        Minimization minimizacao = new Minimization();
                        minimizacao.minimizarAutomato(arquivo.getDoc());
                    }
                }
            }
        }
    }
}
