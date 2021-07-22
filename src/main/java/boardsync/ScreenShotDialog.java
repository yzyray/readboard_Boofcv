package boardsync;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.swing.JDialog;

public class ScreenShotDialog extends JDialog {
  ScreenShotDialog t;
  private int startX;
  private int startY;

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

  private Dimension screenSize;
  private BufferedImage imageSrc;
  private BufferedImage imageTmp;
  private BufferedImage imageShow;
  private BufferedImage imageOut;

  @Override
  public void paint(Graphics g) {
    if (imageShow != null) {
      g.drawImage(imageShow, 0, 0, this);
    }
  }

  private void drawSelectArea() {
    if (imageTmp == null || imageShow == null) return;
    if (orgx == -1 || orgy == -1) return;
    Graphics2D g = (Graphics2D) imageShow.getGraphics();
    int x = Math.min(orgx, endx);
    int y = Math.min(orgy, endy);
    int width = Math.abs(endx - orgx) + 1;
    int height = Math.abs(endy - orgy) + 1;
    imageOut = imageSrc.getSubimage(x, y, width, height);
    startX = x;
    startY = y;
    g.drawImage(imageTmp, 0, 0, this);
    g.setColor(Color.BLUE);
    g.drawRect(x - 1, y - 1, width + 1, height + 1);
    g.drawImage(imageOut, x, y, this);
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
          }
        });
  }

  public ScreenShotDialog(JDialog owner) throws Exception {
    super(owner);
  }

  public void open() throws Exception {
    bindSelectAreaListener();
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    imageSrc = this.screenCapture(screenSize);
    imageTmp = new RescaleOp(0.8f, 0, null).filter(imageSrc, null);
    imageShow = new BufferedImage(imageTmp.getWidth(), imageTmp.getHeight(), TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) imageShow.getGraphics();
    g.drawImage(imageTmp, 0, 0, this);

    this.setUndecorated(true);
    this.setResizable(false);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setLocation(0, 0);
    this.setSize(screenSize);
    this.setVisible(true);
  }

  protected void close() {
    this.dispose();
    BoardSyncTool.screenImage = imageOut;
    BoardSyncTool.screenImageStartX = startX;
    BoardSyncTool.screenImageStartY = startY;
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
