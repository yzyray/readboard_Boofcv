package boardsync;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class BoardOCR {

  public boolean hasMoveAt(int x, int y, int width, int height, int moveX, int moveY)
      throws AWTException {
    if (width <= 0 || height <= 0) return true;
    BufferedImage input = getScreenImage(x, y, width, height);
    boolean hasStone = false;
    if (getColorPercent(input, x, y, width, height, true) >= BoardSyncTool.config.blackPercent)
      hasStone = true;
    int whitePercent = getColorPercent(input, x, y, width, height, false);
    if (whitePercent >= BoardSyncTool.config.whitePercent) {
      if (moveX == 0
          || moveX == BoardSyncTool.boardWidth - 1
          || moveY == 0
          || moveY == BoardSyncTool.boardHeight - 1) {
        if (whitePercent > 85) hasStone = false;
        else hasStone = true;
      } else {
        if (whitePercent > 80) hasStone = false;
        else hasStone = true;
      }
      hasStone = true;
    }
    return hasStone;
  }

  public void oneTimeSync() throws AWTException {
    BoardPosition position = BoardSyncTool.boardPosition;
    BufferedImage input = getScreenImage(position.x, position.y, position.width, position.height);
    recognizeBoard(input);
  }

  private BufferedImage getScreenImage(int x, int y, int width, int height) throws AWTException {
    Robot robot = new Robot();
    return robot.createScreenCapture(new Rectangle(x, y, width, height));
  }

  private void recognizeBoard(BufferedImage input) {
    float hGap = input.getHeight() / (float) BoardSyncTool.boardHeight;
    float vGap = input.getWidth() / (float) BoardSyncTool.boardWidth;
    int hGapInt = Math.round(hGap);
    int vGapInt = Math.round(vGap);
    String result = "";
    int blackMinPercent = 200;
    int blackTotalPercent = 0;
    int blackMinX = -1;
    int blackMinY = -1;
    int blackCounts = 0;

    int whiteMinPercent = 200;
    int whiteTotalPercent = 0;
    int whiteMinX = -1;
    int whiteMinY = -1;
    int whiteCounts = 0;

    int resultValue[] = new int[BoardSyncTool.boardHeight * BoardSyncTool.boardWidth];

    for (int y = 0; y < BoardSyncTool.boardHeight; y++) {
      for (int x = 0; x < BoardSyncTool.boardWidth; x++) {
        int blackPercent =
            getColorPercent(
                input, Math.round(x * vGap), Math.round(y * hGap), vGapInt, hGapInt, true);
        if (blackPercent >= BoardSyncTool.config.blackPercent) {
          resultValue[y * BoardSyncTool.boardWidth + x] = 1;
          blackTotalPercent += blackPercent;
          blackCounts++;
          if (blackPercent < blackMinPercent) {
            blackMinPercent = blackPercent;
            blackMinX = x;
            blackMinY = y;
          }
        } else {
          boolean isWhite = false;
          int whitePercent =
              getColorPercent(
                  input, Math.round(x * vGap), Math.round(y * hGap), vGapInt, hGapInt, false);
          if (whitePercent >= BoardSyncTool.config.whitePercent) {
            if (x == 0
                || x == BoardSyncTool.boardWidth - 1
                || y == 0
                || y == BoardSyncTool.boardHeight - 1) {
              if (whitePercent > 85) isWhite = false;
              else isWhite = true;
            } else {
              if (whitePercent > 80) isWhite = false;
              else isWhite = true;
            }
          }
          if (isWhite) {
            resultValue[y * BoardSyncTool.boardWidth + x] = 2;
            whiteTotalPercent += whitePercent;
            whiteCounts++;
            if (whitePercent < whiteMinPercent) {
              whiteMinPercent = whitePercent;
              whiteMinX = x;
              whiteMinY = y;
            }
          } else resultValue[y * BoardSyncTool.boardWidth + x] = 0;
        }
      }
    }
    if (blackCounts >= 2 && whiteCounts >= 2) {
      float blackMaxOffset =
          Math.abs(
              blackMinPercent - (blackTotalPercent - blackMinPercent) / (float) (blackCounts - 1));
      float whiteMaxOffset =
          Math.abs(
              whiteMinPercent - (whiteTotalPercent - whiteMinPercent) / (float) (whiteCounts - 1));
      if (blackMaxOffset >= whiteMaxOffset) {
        if (blackMinY >= 0 && blackMinX >= 0)
          resultValue[blackMinY * BoardSyncTool.boardWidth + blackMinX] = 3;
      } else {
        if (whiteMinY >= 0 && whiteMinX >= 0)
          resultValue[whiteMinY * BoardSyncTool.boardWidth + whiteMinX] = 4;
      }
    } else if (blackCounts > 0 && whiteCounts > 0) {
      if (blackCounts < whiteCounts) {
        if (blackMinY >= 0 && blackMinX >= 0)
          resultValue[blackMinY * BoardSyncTool.boardWidth + blackMinX] = 3;
      }
      if (blackCounts > whiteCounts) {
        if (whiteMinY >= 0 && whiteMinX >= 0)
          resultValue[whiteMinY * BoardSyncTool.boardWidth + whiteMinX] = 4;
      }
    }

    for (int i = 0; i < BoardSyncTool.boardHeight; i++) {
      for (int j = 0; j < BoardSyncTool.boardWidth; j++) {
        result += resultValue[i * BoardSyncTool.boardWidth + j] + ",";
        if (j == (BoardSyncTool.boardWidth - 1)) {
          result = result.substring(0, result.length() - 1);
          Utils.send("re=" + result);
          result = "";
        }
      }
    }
    Utils.send("end");
  }

  private int getColorPercent(
      BufferedImage input, int startX, int startY, int width, int height, boolean isBlack) {
    int sum = 0;
    if (startX + width > input.getWidth()) startX = input.getWidth() - width;
    if (startY + height > input.getHeight()) startY = input.getHeight() - height;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb[] = getRGB(input, startX + x, startY + y);
        if (Math.abs(rgb[0] - rgb[1]) < BoardSyncTool.config.grayOffset
            && Math.abs(rgb[0] - rgb[2]) < BoardSyncTool.config.grayOffset
            && Math.abs(rgb[1] - rgb[2]) < BoardSyncTool.config.grayOffset) {
          if (isBlack) {
            if (rgb[0] <= BoardSyncTool.config.blackOffset
                && rgb[1] <= BoardSyncTool.config.blackOffset
                && rgb[2] <= BoardSyncTool.config.blackOffset) {
              sum++;
            }
          } else {
            int value = 255 - BoardSyncTool.config.whiteOffset;
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
