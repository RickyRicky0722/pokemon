
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Util {

  /**
   * Jsonファイル読み込み
   * @param filePath Jsonファイルパス
   * @param rootProperty Jsonファイル名
   * @return 取得データ
   */
  public static JsonNode readJson(String filePath, String rootProperty) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = null;
    try {
      // TODO: 2021/09/15 ファイルの存在確認処理の記述
      File file = new File(filePath);
      if(!file.exists()) {
        System.out.println("ファイルが存在しません。" + filePath);
        return  null;
      }
      // ファイルの存在確認fin.
      JsonNode readTree = mapper.readTree(new File(filePath));
      // readTreeにJsonファイルを取り込み
      root = readTree.get(rootProperty);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return root;
  }

}
