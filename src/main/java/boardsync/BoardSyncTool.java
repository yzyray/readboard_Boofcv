package boardsync;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

public class BoardSyncTool {
  public static ResourceBundle resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings");
  public static Language language = Language.Chinese;
  private static boolean useJavaLooks = false;
  private static int fontSize = 12;

  public static int boardWidth = 19;
  public static int boardHeight = 19;

  public static boolean isGettingScreen = false;
  public static BufferedImage screenImage;
  public static int screenImageStartX;
  public static int screenImageStartY;
  public static BoardPosition boardPosition = null;
  public static Config config;
  public static ToolFrame toolFrame;
  public static boolean isWindows =
      System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;

  public static void main(String[] args) {
    // 共传入5个参数,是否中文 是否java外观 字体大小 宽 高
    if (args.length == 5) {
      if (args[0].equals("cn")) language = Language.Chinese;
      else if (args[0].equals("en")) language = Language.English;
      else if (args[0].equals("jp")) language = Language.Japanese;
      else if (args[0].equals("kr")) language = Language.Korean;
      if (args[1].equals("true")) useJavaLooks = true;
      else useJavaLooks = false;
      try {
        fontSize = Integer.parseInt(args[2]);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
      try {
        boardWidth = Integer.parseInt(args[3]);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
      try {
        boardHeight = Integer.parseInt(args[4]);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }
    switch (language) {
      case Chinese:
        resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings", new Locale("zh", "CN"));
        break;
      case English:
        resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings", new Locale("en", "US"));
        break;
      case Japanese:
        resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings", new Locale("ja", "JP"));
        break;
      case Korean:
        resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings", new Locale("ko"));
        break;
    }
    setLookAndFeel();
    try {
      config = new Config();
    } catch (IOException e) {
      e.printStackTrace();
    }
    toolFrame = new ToolFrame();
    toolFrame.setVisible(true);

    startGetInputStreamThread();
  }

  private static void startGetInputStreamThread() {
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    new Thread() {
      public void run() {
        try {
          Robot robot = new Robot();
          String line = "";
          while ((line = inputReader.readLine()) != null) {
            if (line.startsWith("place")) {
              if (toolFrame.chkBothSync.isSelected()
                  && toolFrame.isKeepSyncing
                  && !toolFrame.keepSyncThreadInterrupted) {
                String[] params = line.trim().split(" ");
                if (params.length == 3) {
                  new Thread() {
                    public void run() {
                      place(params[1], params[2], robot);
                    }
                  }.start();
                }
              }
            }
            if (line.equals("quit")) shutdown();
          }
        } catch (IOException | AWTException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  public static void place(String strX, String strY, Robot robot) {
    Point point = MouseInfo.getPointerInfo().getLocation();
    int x = -1;
    int y = -1;
    int maxTimes = 10;
    try {
      x = Integer.parseInt(strX.trim());
      y = Integer.parseInt(strY.trim());
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return;
    }
    float hGap = BoardSyncTool.boardPosition.height / (float) BoardSyncTool.boardHeight;
    float vGap = BoardSyncTool.boardPosition.width / (float) BoardSyncTool.boardWidth;
    int posX = (int) Math.round((x + 0.5) * vGap + BoardSyncTool.boardPosition.x);
    int posY = (int) Math.round((y + 0.5) * hGap + BoardSyncTool.boardPosition.y);
    int verifyX = (int) Math.round(x * vGap + BoardSyncTool.boardPosition.x);
    int verifyY = (int) Math.round(y * hGap + BoardSyncTool.boardPosition.y);
    try {
      do {
        mouseClick(posX, posY, robot);
        maxTimes--;
      } while (BoardSyncTool.config.verifyPlacedMove
          && maxTimes > 0
          && !verifyMove(verifyX, verifyY, Math.round(vGap), Math.round(hGap), x, y));
    } catch (AWTException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    robot.mouseMove((int) point.getX(), (int) point.getY());
  }

  private static boolean verifyMove(int x, int y, int width, int height, int moveX, int moveY)
      throws AWTException {
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    BoardOCR boardOCR = new BoardOCR();
    return boardOCR.hasMoveAt(x, y, width, height, moveX, moveY);
  }

  private static void mouseClick(int posX, int posY, Robot robot) {
    try {
      robot.mouseMove(posX, posY);
      Thread.sleep(30);
      robot.mouseMove(posX, posY);
      robot.mousePress(InputEvent.BUTTON1_MASK);
      Thread.sleep(30);
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
      Thread.sleep(30);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
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
      setUIFont(new javax.swing.plaf.FontUIResource("Dialog.plain", Font.PLAIN, fontSize));
      UIManager.put(
          "OptionPane.buttonFont", new FontUIResource("Dialog.plain", Font.PLAIN, fontSize));
      UIManager.put(
          "OptionPane.messageFont", new FontUIResource("Dialog.plain", Font.PLAIN, fontSize));
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
      e.printStackTrace();
    }
  }

  public static void shutdown() {
    try {
      config.saveAndWriteConfig();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    System.exit(0);
  }

  public enum Language {
    Chinese,
    English,
    Japanese,
    Korean
  }
}
