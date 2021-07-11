package boardsync;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BoardSyncTool {
  public static ResourceBundle resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings");
  private static boolean isChinese = true; // 处理参数传入
  private static boolean useJavaLooks = false; // 处理参数传入
  private static int fontSize = 12; // 处理参数传入

  private static int boardWidth = 19; // 处理参数传入或者配置读取
  private static int boardHeight = 19;

  public static boolean isGettingScreen = false;
  public static BufferedImage screenImage;
  public static int screenImageStartX;
  public static int screenImageStartY;

  public static void main(String[] args) {
    if (isChinese)
      resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings", new Locale("zh", "CN"));
    else resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings", new Locale("en", "US"));
    setLookAndFeel();
    ToolFrame toolFrame = new ToolFrame();
    toolFrame.setVisible(true);
  }

  public static void setUIFont(javax.swing.plaf.FontUIResource f) {
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof javax.swing.plaf.FontUIResource) UIManager.put(key, f);
    }
  }

  public static void setLookAndFeel() {
    try {
      setUIFont(new javax.swing.plaf.FontUIResource("", Font.PLAIN, fontSize));
      if (useJavaLooks) {
        String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();

        UIManager.setLookAndFeel(lookAndFeel);

      } else {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(lookAndFeel);
      }
    } catch (ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
