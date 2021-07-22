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
  private static boolean isChinese = true;
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

  public static void main(String[] args) {
    // 共传入5个参数,是否中文 是否java外观 字体大小 宽 高
    if (args.length == 5) {
      if (args[0].equals("true")) isChinese = true;
      else isChinese = false;
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
    if (isChinese)
      resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings", new Locale("zh", "CN"));
    else resourceBundle = ResourceBundle.getBundle("l10n.DisplayStrings", new Locale("en", "US"));
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
    try {
      int x = Integer.parseInt(strX.trim());
      int y = Integer.parseInt(strY.trim());
      float hGap = BoardSyncTool.boardPosition.height / (float) BoardSyncTool.boardHeight;
      float vGap = BoardSyncTool.boardPosition.width / (float) BoardSyncTool.boardWidth;
      int posX = (int) Math.round((x + 0.5) * vGap + BoardSyncTool.boardPosition.x);
      int posY = (int) Math.round((y + 0.5) * hGap + BoardSyncTool.boardPosition.y);
      robot.mouseMove(posX, posY);
      robot.mousePress(InputEvent.BUTTON1_MASK);
      try {
        Thread.sleep(30);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
      if (config.useDoubleClick) {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        try {
          Thread.sleep(30);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
      }
      robot.mouseMove((int) point.getX(), (int) point.getY());
    } catch (NumberFormatException e) {
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
      setUIFont(new javax.swing.plaf.FontUIResource("", Font.PLAIN, fontSize));
      UIManager.put(
          "OptionPane.buttonFont", new FontUIResource(new Font("", Font.PLAIN, fontSize)));
      UIManager.put(
          "OptionPane.messageFont", new FontUIResource(new Font("", Font.PLAIN, fontSize)));
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
}
