import utilities.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Arquivo arquivo = new Arquivo();
        String caminho = arquivo.obterCaminho();
        if (caminho != null) {
            ValidarAutomato validarAutomato = new ValidarAutomato(arquivo);
            if (validarAutomato.isAfd(arquivo.getDoc())) {
                if (validarAutomato.isComplete(arquivo.getDoc())) {
                    if (validarAutomato.accessibleStates(arquivo.getDoc())) {
                        Minimization minimizacao = new Minimization();
                        System.out.println("Foi");
                        minimizacao.minimizarAutomato(arquivo, validarAutomato);
                    }else{
                        System.out.println(3);
                    }
                }else{
                    System.out.println(2);
                }
            }else{
                System.out.println(1);
            }
        }
    }
}
