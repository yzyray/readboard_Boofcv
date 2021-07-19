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
  }

  public void saveAndWriteConfig() throws IOException {
    config.put("gray-offset", grayOffset);
    config.put("black-offset", blackOffset);
    config.put("white-offset", whiteOffset);
    config.put("black-percent", blackPercent);
    config.put("white-percent", whitePercent);
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
    return config;
  }
}
