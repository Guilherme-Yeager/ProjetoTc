import utilities.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Arquivo arquivo = new Arquivo();
        String caminho = arquivo.obterCaminho();
        if (caminho != null) {
            arquivo.listaEstados(arquivo.getDoc());
            arquivo.listaTransicoes(arquivo.getDoc());
            ValidarAutomato validarAutomato = new ValidarAutomato(arquivo);
            if (validarAutomato.isAfd(arquivo.getDoc())) {
                if (validarAutomato.isComplete(arquivo.getDoc())) {
                    System.out.println("É completo");
                    if (validarAutomato.accessibleStates(arquivo.getDoc())) {
                        Minimization minimizacao = new Minimization();
                        minimizacao.minimizarAutomato(arquivo, validarAutomato);
                    }else{
                        System.out.println("Não é acessível");
                    }
                }else{
                    System.out.println("Não é completo");
                }
            }else{
                System.out.println("Não é afd");
            }
        }
    }
}
