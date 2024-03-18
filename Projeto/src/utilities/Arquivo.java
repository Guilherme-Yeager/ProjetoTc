package utilities;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

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
     * @return Caminho do arquivo
     */
    public String obterCaminho() {
        try {
            FileNameExtensionFilter arqFiltro = new FileNameExtensionFilter("Somente arquivos .jff", "jff");
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.addChoosableFileFilter(arqFiltro);
            jFileChooser.setDialogTitle("Selecione um arquivo .jff");
            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File arquivo = jFileChooser.getSelectedFile();
                return "file:///" + arquivo.getAbsolutePath();
            }else{
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    public Document lerArquivo(){
        try{
            if(this.caminho != null){
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(this.getCaminho());
                return doc;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
