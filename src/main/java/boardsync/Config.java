package boardsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Config {
  private String configFilename = "readboard_boofcv_config.txt";
  public JSONObject config;
  public int grayOffset = 50;
  public int blackOffset = 96;
  public int whiteOffset = 96;
  public int blackPercent = 33;
  public int whitePercent = 33;
  public int keepSyncIntervalMillseconds = 200;
  public boolean autoMinimize = true;
  public int locationX = 50;
  public int locationY = 50;
  public boolean verifyPlacedMove = true;
  public boolean lastTimeBothSync = false;
  public int lastTimeTotalTime = -1;
  public int lastTimeTotalVisits = -1;
  public int lastTimeFirstVisits = -1;

  public Config() throws IOException {
    try {
      config = readConfig();
    } catch (Exception e) {
      e.printStackTrace();
      config = createDefaultConfig();
      writeConfig(config);
    }
    grayOffset = config.optInt("gray-offset", 50);
    blackOffset = config.optInt("black-offset", 96);
    whiteOffset = config.optInt("white-offset", 96);
    blackPercent = config.optInt("black-percent", 33);
    whitePercent = config.optInt("white-percent", 33);
    keepSyncIntervalMillseconds = config.optInt("keep-sync-interval-millseconds", 200);
    autoMinimize = config.optBoolean("auto-minimize", true);
    locationX = config.optInt("location-x", 50);
    locationY = config.optInt("location-y", 50);
    verifyPlacedMove = config.optBoolean("verify-placed-move", true);

    lastTimeBothSync = config.optBoolean("last-time-both-sync", false);
    lastTimeTotalTime = config.optInt("last-time-total-time", -1);
    lastTimeTotalVisits = config.optInt("last-time-total-visits", -1);
    lastTimeFirstVisits = config.optInt("last-time-fitst-visits", -1);
  }

  public void saveAndWriteConfig() throws IOException {

    config.put("gray-offset", grayOffset);
    config.put("black-offset", blackOffset);
    config.put("white-offset", whiteOffset);
    config.put("black-percent", blackPercent);
    config.put("white-percent", whitePercent);
    config.put("keep-sync-interval-millseconds", keepSyncIntervalMillseconds);
    config.put("auto-minimize", autoMinimize);
    config.put("verify-placed-move", verifyPlacedMove);

    locationX = BoardSyncTool.toolFrame.getLocation().x;
    locationY = BoardSyncTool.toolFrame.getLocation().y;
    config.put("location-x", locationX);
    config.put("location-y", locationY);

    lastTimeBothSync = BoardSyncTool.toolFrame.chkBothSync.isSelected();
    lastTimeTotalTime = Utils.parseTextToInt(BoardSyncTool.toolFrame.txtTotalTime, -1);
    lastTimeTotalVisits = Utils.parseTextToInt(BoardSyncTool.toolFrame.txtFirstVisits, -1);
    lastTimeFirstVisits = Utils.parseTextToInt(BoardSyncTool.toolFrame.txtFirstVisits, -1);
    config.put("last-time-both-sync", lastTimeBothSync);
    config.put("last-time-total-time", lastTimeTotalTime);
    config.put("last-time-total-visits", lastTimeTotalVisits);
    config.put("last-time-fitst-visits", lastTimeFirstVisits);
    writeConfig(config);
  }

  private void writeConfig(JSONObject config) throws IOException {
    File file = new File(configFilename);
    file.createNewFile();
    FileOutputStream fp = new FileOutputStream(file);
    OutputStreamWriter writer = new OutputStreamWriter(fp, "utf-8");
    writer.write(config.toString(2));
    writer.close();
    fp.close();
  }

  private JSONObject readConfig() throws FileNotFoundException, UnsupportedEncodingException {
    File file = new File(configFilename);
    FileInputStream fp = new FileInputStream(file);
    InputStreamReader reader = new InputStreamReader(fp, "utf-8");
    return new JSONObject(new JSONTokener(reader));
  }

  private JSONObject createDefaultConfig() {
    JSONObject config = new JSONObject();
    config.put("gray-offset", grayOffset);
    config.put("black-offset", blackOffset);
    config.put("white-offset", whiteOffset);
    config.put("black-percent", blackPercent);
    config.put("white-percent", whitePercent);
    config.put("keep-sync-interval-millseconds", keepSyncIntervalMillseconds);
    config.put("auto-minimize", autoMinimize);
    config.put("verify-placed-move", verifyPlacedMove);

    config.put("location-x", locationX);
    config.put("location-y", locationY);

    config.put("last-time-both-sync", lastTimeBothSync);
    config.put("last-time-total-time", lastTimeTotalTime);
    config.put("last-time-total-visits", lastTimeTotalVisits);
    config.put("last-time-fitst-visits", lastTimeFirstVisits);
    return config;
  }
}
