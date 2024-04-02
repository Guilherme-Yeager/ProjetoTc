package utilities;
import javax.swing.JOptionPane;

public class Error {
    public static void errorMessageAndExit(String message)
    {
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }

    public static void errorMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message);
    }
}
