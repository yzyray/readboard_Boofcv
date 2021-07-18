package boardsync;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class BoardOCR {

  public void oneTimeSync() throws AWTException {
    BoardPosition position = BoardSyncTool.boardPosition;
    BufferedImage input = getScreenImage(position.x, position.y, position.width, position.height);
    Send("start " + BoardSyncTool.boardWidth + " " + BoardSyncTool.boardHeight + " ");
    recognizeBoard(input);
  }

  private BufferedImage getScreenImage(int x, int y, int width, int height) throws AWTException {
    Robot robot = new Robot();
    // robot.mouseMove(x, y);
    return robot.createScreenCapture(new Rectangle(x, y, width, height));
  }

  private void recognizeBoard(BufferedImage input) {
    float hGap = input.getHeight() / (float) BoardSyncTool.boardHeight;
    float vGap = input.getHeight() / (float) BoardSyncTool.boardWidth;
    int hGapInt = Math.round(hGap);
    int vGapInt = Math.round(vGap);
    String result = "";
    for (int i = 0; i < BoardSyncTool.boardHeight; i++) {
      for (int j = 0; j < BoardSyncTool.boardWidth; j++) {
        if (getColorPercent(
                input, Math.round(j * vGap), Math.round(i * hGap), vGapInt, hGapInt, true)
            >= BoardSyncTool.blackPercent) {
          result = result + "1,";
        } else {
          int whitePercent =
              getColorPercent(
                  input, Math.round(j * vGap), Math.round(i * hGap), vGapInt, hGapInt, false);
          if (whitePercent >= BoardSyncTool.whitePercent) {
            if (j == 0
                || j == BoardSyncTool.boardWidth - 1
                || i == 0
                || i == BoardSyncTool.boardHeight - 1) {
              if (whitePercent > 85) result = result + "0,";
              else result = result + "2,";
            } else {
              if (whitePercent > 80) result = result + "0,";
              else result = result + "2,";
            }
          } else result = result + "0,";
        }
        if (j == (BoardSyncTool.boardWidth - 1)) {
          result = result.substring(0, result.length() - 1);
          Send("re=" + result);
          result = "";
        }
      }
    }
    Send("end");
  }

  private void Send(String message) {
    System.out.println(message);
  }

  private int getColorPercent(
      BufferedImage input, int startX, int startY, int width, int height, boolean isBlack) {
    int sum = 0;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb[] = getRGB(input, startX + x, startY + y);
        if (Math.abs(rgb[0] - rgb[1]) < 50
            && Math.abs(rgb[0] - rgb[2]) < 50
            && Math.abs(rgb[1] - rgb[2]) < 50) {
          if (isBlack) {
            if (rgb[0] <= BoardSyncTool.blackOffset
                && rgb[1] <= BoardSyncTool.blackOffset
                && rgb[2] <= BoardSyncTool.blackOffset) {
              sum++;
            }
          } else {
            int value = 255 - BoardSyncTool.whiteOffset;
            if (rgb[0] >= value && rgb[1] >= value && rgb[2] >= value) {
              sum++;
            }
          }
        }
      }
    }
    return (100 * sum) / (width * height);
  }

  private int[] getRGB(BufferedImage image, int x, int y) {
    int[] rgb = null;

    rgb = new int[3];
    int pixel = image.getRGB(x, y);
    rgb[0] = (pixel & 0xff0000) >> 16;
    rgb[1] = (pixel & 0xff00) >> 8;
    rgb[2] = (pixel & 0xff);

    return rgb;
  }
}
