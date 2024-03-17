package utilities;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Arquivo {
    private String caminho;

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    /**
     * Método utilizado para definir o atributo caminho de acordo com arquivo
     * selecionado pelo usuário
     * 
     */
    public void obterCaminho() {
        try {
            FileNameExtensionFilter arqFiltro = new FileNameExtensionFilter("Somente arquivos .jff", "jff");
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.addChoosableFileFilter(arqFiltro);
            jFileChooser.setDialogTitle("Selecione um arquivo .jff");
            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File arquivo = jFileChooser.getSelectedFile();
                this.setCaminho("file:///" + arquivo.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }

}
