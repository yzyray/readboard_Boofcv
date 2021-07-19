package boardsync;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Utils {

  public static void showMssage(Component parentComponent, String message, String title) {
    SwingUtilities.invokeLater(
        new Runnable() {
          public void run() {
            JOptionPane.showMessageDialog(
                parentComponent, message, title, JOptionPane.WARNING_MESSAGE);
          }
        });
  }

  public static void send(String message) {
    System.out.println(message);
  }
}
