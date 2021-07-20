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
  public boolean lastTimeBothSync = false;
  public boolean useDoubleClick = true;

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
    lastTimeBothSync = config.optBoolean("last-time-both-sync", false);
    useDoubleClick = config.optBoolean("use-double-click", true);
  }

  public void saveAndWriteConfig() throws IOException {

    config.put("gray-offset", grayOffset);
    config.put("black-offset", blackOffset);
    config.put("white-offset", whiteOffset);
    config.put("black-percent", blackPercent);
    config.put("white-percent", whitePercent);
    config.put("keep-sync-interval-millseconds", keepSyncIntervalMillseconds);
    config.put("auto-minimize", autoMinimize);
    locationX = BoardSyncTool.toolFrame.getLocation().x;
    locationY = BoardSyncTool.toolFrame.getLocation().y;
    lastTimeBothSync = BoardSyncTool.toolFrame.chkBothSync.isSelected();
    config.put("location-x", locationX);
    config.put("location-y", locationY);
    config.put("last-time-both-sync", lastTimeBothSync);
    config.put("use-double-click", useDoubleClick);
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
    config.put("location-x", locationX);
    config.put("location-y", locationY);
    config.put("last-time-both-sync", lastTimeBothSync);
    config.put("use-double-click", useDoubleClick);
    return config;
  }
}
