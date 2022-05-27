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
    MoveColorInfo colorInfo = getColorPercent(input, x, y, width, height, false);
    if (colorInfo.blackPercent >= BoardSyncTool.config.blackPercent) return true;
    if (colorInfo.whitePercent >= BoardSyncTool.config.whitePercent) {
      if (!colorInfo.trueWhite && colorInfo.almostWhitePercent - colorInfo.pureWhitePercent < 10)
        hasStone = false;
      else hasStone = true;
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
    int redCount = 0;
    int blueCount = 0;
    int blueRedX = -1;
    int blueRedY = -1;
    boolean needCheckRedBlue = true;

    for (int y = 0; y < BoardSyncTool.boardHeight; y++) {
      for (int x = 0; x < BoardSyncTool.boardWidth; x++) {
        MoveColorInfo colorInfo =
            getColorPercent(
                input,
                Math.round(x * vGap),
                Math.round(y * hGap),
                vGapInt,
                hGapInt,
                needCheckRedBlue);
        boolean isBlack = colorInfo.blackPercent >= BoardSyncTool.config.blackPercent;
        boolean isWhite = false;
        if (isBlack) {
          resultValue[y * BoardSyncTool.boardWidth + x] = 1;
          blackTotalPercent += colorInfo.blackPercent;
          blackCounts++;
          if (colorInfo.blackPercent < blackMinPercent) {
            blackMinPercent = colorInfo.blackPercent;
            blackMinX = x;
            blackMinY = y;
          }
        } else {
          if (colorInfo.whitePercent >= BoardSyncTool.config.whitePercent) {
            if (!colorInfo.trueWhite
                && colorInfo.almostWhitePercent - colorInfo.pureWhitePercent < 10) isWhite = false;
            else isWhite = true;
          }
          if (isWhite) {
            resultValue[y * BoardSyncTool.boardWidth + x] = 2;
            whiteTotalPercent += colorInfo.whitePercent;
            whiteCounts++;
            if (colorInfo.whitePercent < whiteMinPercent) {
              whiteMinPercent = colorInfo.whitePercent;
              whiteMinX = x;
              whiteMinY = y;
            }
          } else resultValue[y * BoardSyncTool.boardWidth + x] = 0;
        }
        if (needCheckRedBlue && (isWhite || isBlack)) {
          if (colorInfo.bluePercent >= BoardSyncTool.config.bluePercent) {
            blueCount++;
            if (redCount > 1 && blueCount > 1) needCheckRedBlue = false;
            else {
              blueRedX = x;
              blueRedY = y;
            }
          }
          if (colorInfo.redPercent >= BoardSyncTool.config.redPercent) {
            redCount++;
            if (redCount > 1 && blueCount > 1) needCheckRedBlue = false;
            else {
              blueRedX = x;
              blueRedY = y;
            }
          }
        }
      }
    }
    if ((redCount == 1 && blueCount != 1) || (redCount != 1 && blueCount == 1)) {
      if (resultValue[blueRedY * BoardSyncTool.boardWidth + blueRedX] == 1)
        resultValue[blueRedY * BoardSyncTool.boardWidth + blueRedX] = 3;
      else if (resultValue[blueRedY * BoardSyncTool.boardWidth + blueRedX] == 2)
        resultValue[blueRedY * BoardSyncTool.boardWidth + blueRedX] = 4;
    } else {
      if (blackCounts >= 2 && whiteCounts >= 2) {
        float blackMaxOffset =
            Math.abs(
                blackMinPercent
                    - (blackTotalPercent - blackMinPercent) / (float) (blackCounts - 1));
        float whiteMaxOffset =
            Math.abs(
                whiteMinPercent
                    - (whiteTotalPercent - whiteMinPercent) / (float) (whiteCounts - 1));
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

  private MoveColorInfo getColorPercent(
      BufferedImage input,
      int startX,
      int startY,
      int width,
      int height,
      boolean needCheckRedBlue) {
    int blackSum = 0;
    int whiteSum = 0;
    int redSum = 0;
    int blueSum = 0;
    int pureWhiteSum = 0;
    int almostWhiteSum = 0;
    boolean trueWhite = false;
    if (startX + width > input.getWidth()) startX = input.getWidth() - width;
    if (startY + height > input.getHeight()) startY = input.getHeight() - height;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb[] = getRGB(input, startX + x, startY + y);
        int red = rgb[0];
        int blue = rgb[1];
        int green = rgb[2];
        if (Math.abs(red - blue) < BoardSyncTool.config.grayOffset
            && Math.abs(blue - green) < BoardSyncTool.config.grayOffset
            && Math.abs(green - red) < BoardSyncTool.config.grayOffset) {
          if (red <= BoardSyncTool.config.blackOffset
              && blue <= BoardSyncTool.config.blackOffset
              && green <= BoardSyncTool.config.blackOffset) {
            blackSum++;
          }
          int whiteValue = 255 - BoardSyncTool.config.whiteOffset;
          if (red >= whiteValue && blue >= whiteValue && green >= whiteValue) {
            whiteSum++;
          }
          int pureWhiteValue = 255 - 30;
          if (red >= pureWhiteValue && blue >= pureWhiteValue && green >= pureWhiteValue) {
            pureWhiteSum++;
          }
          int almostWhiteValue = 255 - 65;
          if (red >= almostWhiteValue && blue >= almostWhiteValue && green >= almostWhiteValue) {
            almostWhiteSum++;
          }
        }
        if (needCheckRedBlue) {
          if (red >= 150 && blue <= 50 && green <= 50) {
            redSum++;
          }
          if (blue >= 150 && red <= 50 && green <= 50) {
            blueSum++;
          }
        }
      }
    }
    MoveColorInfo colorInfo = new MoveColorInfo();
    int total = width * height;
    colorInfo.blackPercent = (100 * blackSum) / total;
    colorInfo.whitePercent = (100 * whiteSum) / total;
    colorInfo.redPercent = (100 * redSum) / total;
    colorInfo.bluePercent = (100 * blueSum) / total;
    colorInfo.pureWhitePercent = (100 * pureWhiteSum) / total;
    colorInfo.almostWhitePercent = (100 * almostWhiteSum) / total;
    if (colorInfo.whitePercent >= BoardSyncTool.config.whitePercent) {
      int y = (int) Math.round(height / 4.0);
      int pureWhiteValue = 255 - 30;
      for (int x = 0; x < (int) Math.round(width * 2.0 / 6.0); x++) {
        int rgb[] = getRGB(input, startX + x, startY + y);
        int red = rgb[0];
        int blue = rgb[1];
        int green = rgb[2];
        if (red < pureWhiteValue || blue < pureWhiteValue || green < pureWhiteValue) {
          trueWhite = true;
          break;
        }
      }
    }
    colorInfo.trueWhite = trueWhite;
    return colorInfo;
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
