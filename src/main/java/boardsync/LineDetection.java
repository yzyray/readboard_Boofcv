package boardsync;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import boofcv.abst.feature.detect.line.DetectLineSegment;
import boofcv.factory.feature.detect.line.ConfigLineRansac;
import boofcv.factory.feature.detect.line.FactoryDetectLine;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.feature.ImageLinePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.ImageGray;
import georegression.struct.line.LineSegment2D_F32;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LineDetection {
  private boolean showDebugImages = false;
  private int size;
  // adjusts edge threshold for identifying pixels belonging to a line
  //  private static final float edgeThreshold = 25;
  // adjust the maximum number of found lines in the image
  // private static final int maxLines = 10;

  //  private BufferedImage edgeDetect(BufferedImage image) {
  //
  //    GrayU8 gray = ConvertBufferedImage.convertFrom(image, (GrayU8) null);
  //    GrayU8 edgeImage = gray.createSameShape();
  //
  //    // Create a canny edge detector which will dynamically compute the threshold based on
  // maximum
  //    // edge intensity
  //    // It has also been configured to save the trace as a graph.  This is the graph created
  // while
  //    // performing
  //    // hysteresis thresholding.
  //    CannyEdge<GrayU8, GrayS16> canny =
  //        FactoryEdgeDetectors.canny(2, true, true, GrayU8.class, GrayS16.class);
  //
  //    // The edge image is actually an optional parameter.  If you don't need it just pass in null
  //    canny.process(gray,0.1f,0.3f,edgeImage);
  //
  //    // First get the contour created by canny
  //    //	List<EdgeContour> edgeContours = canny.getContours();
  //    // The 'edgeContours' is a tree graph that can be difficult to process.  An alternative is
  // to
  //    // extract
  //    // the contours from the binary image, which will produce a single loop for each connected
  //    // cluster of pixels.
  //    // Note that you are only interested in external contours.
  //    //	List<Contour> contours = BinaryImageOps.contourExternal(edgeImage, ConnectRule.EIGHT);
  //
  //    // display the results
  //    BufferedImage visualBinary = VisualizeBinaryData.renderBinary(edgeImage, false, null);
  //
  //    //	BufferedImage visualCannyContour = VisualizeBinaryData.renderContours(edgeContours,null,
  //    //			gray.width,gray.height,null);
  //    // BufferedImage visualEdgeContour = new BufferedImage(gray.width,
  //    // gray.height,BufferedImage.TYPE_INT_RGB);
  //    //	VisualizeBinaryData.render(contours, (int[]) null, visualEdgeContour);
  //
  //    ListDisplayPanel panel = new ListDisplayPanel();
  //    panel.addImage(visualBinary, "Binary Edges from Canny");
  //    // panel.addImage(visualCannyContour, "Canny Trace Graph");
  //    //	panel.addImage(visualEdgeContour,"Contour from Canny Binary");
  //    ShowImages.showWindow(panel, "Canny Edge", true);
  //    return visualBinary;
  //  }

  //  private BufferedImage showSelectedColor(BufferedImage input) {
  //    int width = input.getWidth();
  //    int height = input.getHeight();
  //    int whiteValue = getColorRGBValue(Color.WHITE);
  //
  //    // step through each pixel and mark how close it is to the selected color
  //    BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  //    for (int y = 0; y < height; y++) {
  //      for (int x = 0; x < width; x++) {
  //        // Hue is an angle in radians, so simple subtraction doesn't work
  //        int rgb[] = getRGB(input, x, y);
  // if(Math.abs(rgb[0]-rgb[1])<100&&Math.abs(rgb[0]-rgb[2])<100&&Math.abs(rgb[1]-rgb[2])<100)
  // {
  //	if (rgb[0] < 150 && rgb[1] < 150 && rgb[2] < 150) {
  //          output.setRGB(x, y, input.getRGB(x, y));
  //        } else output.setRGB(x, y, whiteValue);
  //      }
  // else output.setRGB(x, y, whiteValue);
  //    }
  //    }
  //
  //    ShowImages.showWindow(output, "Color In Range");
  //    return output;
  //  }
  //
  //  private int getColorRGBValue(Color color) {
  //    int red = color.getRed();
  //    int green = color.getGreen();
  //    int blue = color.getBlue();
  //    int rgb = red;
  //    rgb = (rgb << 8) + green;
  //    rgb = (rgb << 8) + blue;
  //    return rgb;
  //  }
  //
  //  private int[] getRGB(BufferedImage image, int x, int y) {
  //    int[] rgb = null;
  //
  //    rgb = new int[3];
  //    int pixel = image.getRGB(x, y);
  //    rgb[0] = (pixel & 0xff0000) >> 16;
  //    rgb[1] = (pixel & 0xff00) >> 8;
  //    rgb[2] = (pixel & 0xff);
  //
  //    return rgb;
  //  }

  private <T extends ImageGray<T>, D extends ImageGray<D>>
      List<LineSegment2D_F32> detectLineSegments(
          BufferedImage image, Class<T> imageType, int threshold) {
    // convert the line into a single band image
    size = Math.min(image.getHeight(), image.getWidth());
    T input = ConvertBufferedImage.convertFromSingle(image, null, imageType);

    // Comment/uncomment to try a different type of line detector
    DetectLineSegment<T> detector =
        FactoryDetectLine.lineRansac(
            new ConfigLineRansac(size / 40, threshold, 0.1, true), imageType);

    List<LineSegment2D_F32> found = detector.detect(input);

    if (showDebugImages) {
      System.out.println("Lines: " + found.size());
      // display the results
      ImageLinePanel gui = new ImageLinePanel();
      gui.setImage(image);
      gui.setLineSegments(found);
      gui.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
      ListDisplayPanel listPanel = new ListDisplayPanel();
      listPanel.addItem(gui, "Line Segments");
      ShowImages.showWindow(listPanel, "Detected Lines");
    }
    return found;
  }

  private void CalculateLines(List<LineSegment2D_F32> lines, BufferedImage srcImage) {
    for (LineSegment2D_F32 line : lines) {
      int minX = Math.round(Math.min(line.a.x, line.b.x));
      int maxX = Math.round(Math.max(line.a.x, line.b.x));
      int minY = Math.round(Math.min(line.a.y, line.b.y));
      int maxY = Math.round(Math.max(line.a.y, line.b.y));
      int gapX = maxX - minX;
      int gapY = maxY - minY;
      if (gapX <= 4 && gapY >= 4 * gapX) {
        VerticalHorizonLines vLine = new VerticalHorizonLines();
        vLine.start = minY;
        vLine.end = maxY;
        vLine.position = (minX + maxX) / 2f;
        vLine.lengthSum = gapY;
        verticalLines.add(vLine);
      } else if (gapY <= 4 && gapX >= 4 * gapY) {
        VerticalHorizonLines hLine = new VerticalHorizonLines();
        hLine.start = minX;
        hLine.end = maxX;
        hLine.position = (minY + maxY) / 2f;
        hLine.lengthSum = gapX;
        horizonLines.add(hLine);
      }
    }

    SortLines(horizonLines);
    SortLines(verticalLines);

    if (showDebugImages) {
      BufferedImage linesImage =
          new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), TYPE_INT_ARGB);
      Graphics2D g = (Graphics2D) linesImage.getGraphics();
      g.drawImage(srcImage, 0, 0, null);
      g.setColor(Color.GREEN);
      g.setStroke(new BasicStroke(1));
      for (VerticalHorizonLines line : horizonLines) {
        g.drawLine(line.start, Math.round(line.position), line.end, Math.round(line.position));
      }
      g.setColor(Color.BLUE);
      for (VerticalHorizonLines line : verticalLines) {
        g.drawLine(Math.round(line.position), line.start, Math.round(line.position), line.end);
      }
      ShowImages.showWindow(linesImage, "Lines");
    }
  }

  private int calculateGap(List<VerticalHorizonLines> Lines) {
    // TODO Auto-generated method stub
    List<Float> gaps = new ArrayList<Float>();
    for (int s = 0; s < Lines.size() - 1; s++) {
      VerticalHorizonLines line1 = Lines.get(s);
      VerticalHorizonLines line2 = Lines.get(s + 1);
      float gap = line2.position - line1.position;
      gaps.add(gap);
    }

    List<GapCount> gapCounts = new ArrayList<GapCount>();
    for (int s = 0; s < gaps.size(); s++) {
      float gap = gaps.get(s);
      Boolean isNew = true;
      for (int n = 0; n < gapCounts.size(); n++) {
        GapCount gapCount = gapCounts.get(n);
        if (gap == gapCount.gap) {
          isNew = false;
          gapCount.counts++;
        }
      }
      if (isNew) {
        GapCount gapCount = new GapCount();
        gapCount.gap = gap;
        gapCount.counts = 1;
        gapCounts.add(gapCount);
      }
    }
    Collections.sort(
        gapCounts,
        new Comparator<GapCount>() {
          @Override
          public int compare(GapCount gap1, GapCount gap2) {
            if (gap1.counts > gap2.counts) return -1;
            if (gap1.counts < gap2.counts) return 1;
            return 0;
          }
        });

    float finalGap = gapCounts.get(0).gap;
    //  if (gapCounts.size() == 1)
    return Math.round(finalGap);
    //    List<GapCount> needAvgGaps = new ArrayList<GapCount>();
    //    needAvgGaps.add(gapCounts.get(0));
    //    for (int s = 1; s < 5 && s < gapCounts.size(); s++) {
    //      if (Math.abs(gapCounts.get(s).gap - finalGap) < 3
    //          && gapCounts.get(s).counts > gapCounts.get(0).counts / 3) {
    //        needAvgGaps.add(gapCounts.get(s));
    //      }
    //    }
    //    if (needAvgGaps.size() > 1) {
    //      int all = 0;
    //      int allCounts=0;
    //      for (int s = 0; s < needAvgGaps.size(); s++) {
    //        all += needAvgGaps.get(s).counts*needAvgGaps.get(s).gap;
    //        allCounts+=needAvgGaps.get(s).counts;
    //      }
    //      return Math.round(all / allCounts);
    //    } else return Math.round(finalGap);
  }

  private void SortLines(List<VerticalHorizonLines> lines) {
    // TODO Auto-generated method stub
    Collections.sort(
        lines,
        new Comparator<VerticalHorizonLines>() {
          @Override
          public int compare(VerticalHorizonLines line1, VerticalHorizonLines line2) {
            if (line1.position > line2.position) return 1;
            if (line1.position < line2.position) return -1;
            return 0;
          }
        });
    if (lines.size() >= 2) {
      for (int s = 0; s < lines.size() / 2; s++) {
        VerticalHorizonLines line1 = lines.get(s);
        VerticalHorizonLines line2 = lines.get(s + 1);
        if (Math.abs(line1.position - line2.position) <= 4) {
          line2.position = line1.lengthSum > line2.lengthSum ? line1.position : line2.position;
          line2.lengthSum = line2.lengthSum + line1.lengthSum;
          line2.start = Math.min(line1.start, line2.start);
          line2.end = Math.max(line1.end, line2.end);
          line1.needDelete = true;
        }
      }
      for (int s = lines.size(); s > lines.size() / 2; s--) {
        VerticalHorizonLines line1 = lines.get(s - 1);
        VerticalHorizonLines line2 = lines.get(s - 2);
        if (line2.needDelete) continue;
        if (Math.abs(line1.position - line2.position) <= 4) {
          line2.position = line1.lengthSum > line2.lengthSum ? line1.position : line2.position;
          line2.lengthSum = line2.lengthSum + line1.lengthSum;
          line2.start = Math.min(line1.start, line2.start);
          line2.end = Math.max(line1.end, line2.end);
          line1.needDelete = true;
        }
      }
    }
    for (int s = 0; s < lines.size(); s++) {
      if (lines.get(s).needDelete) {
        lines.remove(s);
        s--;
      } else if (lines.get(s).lengthSum < size / 30) {
        lines.remove(s);
        s--;
      }
    }
  }

  private void validateLines(List<VerticalHorizonLines> Lines, int rawGap) {
    for (int n = 0; n < Lines.size(); n++) {
      VerticalHorizonLines line1 = Lines.get(n);
      for (int m = n + 1; m < Lines.size(); m++) {
        VerticalHorizonLines line2 = Lines.get(m);
        if (Math.abs(line2.position - line1.position - rawGap) <= 2) {
          line1.validate = true;
          line2.validate = true;
        }
      }
    }
  }

  private int reCalculateGap(List<VerticalHorizonLines> Lines, int rawGap) {
    // TODO Auto-generated method stub
    List<GapCount> gapCounts = new ArrayList<GapCount>();
    for (int n = 0; n < Lines.size(); n++) {
      VerticalHorizonLines line1 = Lines.get(n);
      if (!line1.validate) continue;
      for (int m = n + 1; m < Lines.size(); m++) {
        VerticalHorizonLines line2 = Lines.get(m);
        if (!line2.validate) continue;
        float totalGaps = line2.position - line1.position;
        int gap = Math.round(totalGaps / (Math.round(totalGaps / rawGap)));
        Boolean isNew = true;
        for (int s = 0; s < gapCounts.size(); s++) {
          GapCount gapCount = gapCounts.get(s);
          if (gap == gapCount.gap) {
            isNew = false;
            gapCount.counts++;
          }
        }
        if (isNew) {
          GapCount gapCount = new GapCount();
          gapCount.gap = gap;
          gapCount.counts = 1;
          gapCounts.add(gapCount);
        }
      }
    }
    Collections.sort(
        gapCounts,
        new Comparator<GapCount>() {
          @Override
          public int compare(GapCount gap1, GapCount gap2) {
            if (gap1.counts > gap2.counts) return -1;
            if (gap1.counts < gap2.counts) return 1;
            return 0;
          }
        });
    if (gapCounts.get(0).counts >= 3) return (int) gapCounts.get(0).gap;
    else return rawGap;
  }

  private List<lineInterval> calculateInterval(List<VerticalHorizonLines> lines, int length) {
    // TODO Auto-generated method stub
    List<lineInterval> lineIntervals = new ArrayList<lineInterval>();
    for (int s = 0; s < lines.size(); s++) {
      VerticalHorizonLines line1 = lines.get(s);
      for (int n = s + 1; n < lines.size(); n++) {
        VerticalHorizonLines line2 = lines.get(n);
        lineInterval interval = new lineInterval();
        interval.minPos = line1.position;
        interval.maxPos = line2.position;
        interval.interval = Math.abs(line2.position - line1.position - length);
        lineIntervals.add(interval);
      }
    }
    Collections.sort(
        lineIntervals,
        new Comparator<lineInterval>() {
          @Override
          public int compare(lineInterval interval1, lineInterval interval2) {
            if (interval1.interval > interval2.interval) return 1;
            if (interval1.interval < interval2.interval) return -1;
            return 0;
          }
        });
    return lineIntervals;
  }

  private void reCalculateInterval(
      List<lineInterval> lineIntervals, List<VerticalHorizonLines> lines, int gap) {
    // TODO Auto-generated method stub
    while (lineIntervals.size() >= 2
        && Math.abs(lineIntervals.get(0).interval - lineIntervals.get(1).interval) <= gap / 3) {
      float v0Min = lineIntervals.get(0).minPos + gap / 2f;
      int v0MinCounts = 0;
      float v0Max = lineIntervals.get(0).maxPos - gap / 2f;
      int v0MaxCounts = 0;

      float v1Min = lineIntervals.get(1).minPos + gap / 2f;
      int v1MinCounts = 0;
      float v1Max = lineIntervals.get(1).maxPos - gap / 2f;
      int v1MaxCounts = 0;

      for (int s = 0; s < lines.size(); s++) {
        VerticalHorizonLines line = lines.get(s);
        if (!line.validate) continue;
        if (line.start < v0Min && line.end > v0Min) v0MinCounts++;
        if (line.end > v0Max && line.start < v0Max) v0MaxCounts++;
        if (line.start < v1Min && line.end > v1Min) v1MinCounts++;
        if (line.end > v1Max && line.start < v1Max) v1MaxCounts++;
      }

      int v0MinOutLinesCounts = Math.min(v0MinCounts, v0MaxCounts);
      int v1MinOutLinesCounts = Math.min(v1MinCounts, v1MaxCounts);
      if (v0MinOutLinesCounts < v1MinOutLinesCounts) lineIntervals.remove(0);
      else lineIntervals.remove(1);
    }
  }

  private List<VerticalHorizonLines> verticalLines = new ArrayList<VerticalHorizonLines>();
  private List<VerticalHorizonLines> horizonLines = new ArrayList<VerticalHorizonLines>();

  private List<lineInterval> verticalLineIntervals = new ArrayList<lineInterval>();
  private List<lineInterval> horizonLineIntervals = new ArrayList<lineInterval>();

  public BoardPosition getBoardPosition(BufferedImage input) {
    double threshold = 50;
    List<LineSegment2D_F32> lines = detectLineSegments(input, GrayF32.class, (int) threshold);
    CalculateLines(lines, input);
    int lineCounts = verticalLines.size() + horizonLines.size();
    int maxTimes = 10;
    while (lineCounts < 10 && maxTimes > 0) {
      threshold = threshold * 0.9;
      lines = detectLineSegments(input, GrayF32.class, (int) threshold);
      CalculateLines(lines, input);
      lineCounts = verticalLines.size() + horizonLines.size();
      maxTimes--;
    }
    if (horizonLines.size() < 3 || verticalLines.size() < 3) return null;
    int hGap = calculateGap(horizonLines);
    int vGap = calculateGap(verticalLines);

    validateLines(horizonLines, hGap);
    validateLines(verticalLines, vGap);
    if (showDebugImages) {
      System.out.println("H rawGap: " + hGap);
      System.out.println("V rawGap: " + vGap);
    }

    hGap = reCalculateGap(horizonLines, hGap);
    vGap = reCalculateGap(verticalLines, vGap);
    if (showDebugImages) {
      System.out.println("H gap: " + hGap);
      System.out.println("V gap: " + vGap);
    }
    int boardWidth = vGap * (BoardSyncTool.boardWidth - 1);
    int boardHeight = hGap * (BoardSyncTool.boardHeight - 1);

    horizonLineIntervals = calculateInterval(horizonLines, boardHeight);
    verticalLineIntervals = calculateInterval(verticalLines, boardWidth);

    reCalculateInterval(horizonLineIntervals, verticalLines, hGap);
    reCalculateInterval(verticalLineIntervals, horizonLines, vGap);

    int x0 = Math.round(verticalLineIntervals.get(0).minPos - (hGap + 1) / 2);
    int x1 = Math.round(verticalLineIntervals.get(0).maxPos + (hGap + 1) / 2);
    int y0 = Math.round(horizonLineIntervals.get(0).minPos - (vGap + 1) / 2);
    int y1 = Math.round(horizonLineIntervals.get(0).maxPos + (vGap + 1) / 2);
    if (showDebugImages) {
      BufferedImage boardImage =
          new BufferedImage(input.getWidth(), input.getHeight(), TYPE_INT_ARGB);
      Graphics2D g = (Graphics2D) boardImage.getGraphics();
      g.drawImage(input, 0, 0, null);
      g.setColor(Color.GREEN);
      g.setStroke(new BasicStroke(1));
      g.drawRect(x0, y0, x1 - x0, y1 - y0);
      ShowImages.showWindow(boardImage, "Row1");
    }

    BoardPosition position = new BoardPosition();
    position.x = BoardSyncTool.screenImageStartX + x0;
    position.y = BoardSyncTool.screenImageStartY + y0;
    position.width = x1 - x0;
    position.height = y1 - y0;
    position.hGap = hGap;
    position.vGap = vGap;
    return position;
  }
}
