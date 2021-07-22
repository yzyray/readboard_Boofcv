package boardsync;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JDialog;

public class ScreenShotDialog extends JDialog {
  ScreenShotDialog t;
  private int startX;
  private int startY;
  private int capWidth;
  private int capHeight;

  public ScreenShotDialog() {
    try {
      t =
          new ScreenShotDialog(null) {
            private static final long serialVersionUID = 1L;

            protected void capture() {
              super.capture();
            }
          };
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void start() {
    try {
      t.open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static final long serialVersionUID = 1L;

  private int orgx, orgy, endx, endy;
  private int orgxMouse, orgyMouse, endxMouse, endyMouse;

  private Dimension screenSize;
  private BufferedImage imageShow;
  private BufferedImage imageOut;
  private Color backGroundColor = new Color(0, 0, 0, 25);

  @Override
  public void paint(Graphics g) {
    ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
    if (imageShow != null) {
      g.drawImage(imageShow, 0, 0, this);
    }
  }

  private void drawSelectArea() {
    if (orgx == -1 || orgy == -1) return;
    imageShow = new BufferedImage(getWidth(), getHeight(), TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) imageShow.getGraphics();
    int x = Math.min(orgx, endx);
    int y = Math.min(orgy, endy);
    int width = Math.abs(endx - orgx) + 1;
    int height = Math.abs(endy - orgy) + 1;
    startX = Math.min(orgxMouse, endxMouse);
    startY = Math.min(orgyMouse, endyMouse);
    capWidth = width;
    capHeight = height;
    g.setBackground(backGroundColor);
    g.clearRect(x, y, width, height);
    g.setColor(Color.BLUE);
    g.setStroke(new BasicStroke(2));
    g.drawRect(x, y, width, height);
    repaint();
    //		int tx = endx + 5;
    //		int ty = endy + 20;
    //		if (tx + 100 > screenSize.width || ty + 30 > screenSize.height) {
    //			tx = endx - 100;
    //			ty = endy - 30;
    //		}
    //		g.setColor(Color.RED);
    //		g.drawString("w: " + width + ", h: " + height, tx, ty);
  }

  private void bindSelectAreaListener() {
    this.addMouseListener(
        new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
              orgx = e.getX();
              orgy = e.getY();
              Point point = MouseInfo.getPointerInfo().getLocation();
              orgxMouse = point.x;
              orgyMouse = point.y;
            }
          }

          public void mouseReleased(MouseEvent e) {
            orgx = -1;
            orgy = -1;
            capture();
          }
        });
    this.addMouseMotionListener(
        new MouseMotionAdapter() {
          public void mouseDragged(MouseEvent e) {
            endx = e.getX();
            endy = e.getY();
            drawSelectArea();
            Point point = MouseInfo.getPointerInfo().getLocation();
            endxMouse = point.x;
            endyMouse = point.y;
          }
        });
  }

  public ScreenShotDialog(JDialog owner) throws Exception {
    super(owner);
  }

  public void open() throws Exception {
    bindSelectAreaListener();
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setUndecorated(true);
    this.setBackground(backGroundColor);
    this.setSize(screenSize);
    this.setLocation(0, 0);
    this.setVisible(true);
  }

  protected void close() {
    if (capWidth > 0 && capHeight > 0) {
      Robot robot;
      try {
        robot = new Robot();
        imageOut = robot.createScreenCapture(new Rectangle(startX, startY, capWidth, capHeight));
      } catch (AWTException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      BoardSyncTool.screenImage = imageOut;
      BoardSyncTool.screenImageStartX = startX;
      BoardSyncTool.screenImageStartY = startY;
    }
    this.dispose();
    BoardSyncTool.isGettingScreen = false;
  }

  protected void capture() {
    close();
  }

  public BufferedImage screenCapture(Dimension screenSize) throws AWTException {
    Robot robot = new Robot();
    return robot.createScreenCapture(new Rectangle(0, 0, screenSize.width, screenSize.height));
  }
}
