package boardsync;

import java.awt.AWTException;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Utils {

  public static void showMssage(Component parentComponent, String message, String title) {
    SwingUtilities.invokeLater(
        new Runnable() {
          public void run() {
            if (BoardSyncTool.boardPosition == null) {
              JOptionPane.showMessageDialog(
                  parentComponent, message, title, JOptionPane.WARNING_MESSAGE);
            } else {
              BoardOCR boardOCR = new BoardOCR();
              try {
                boardOCR.oneTimeSync();
              } catch (AWTException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
          }
        });
  }

  public static void send(String message) {
    System.out.println(message);
  }
}
