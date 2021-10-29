
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Skill {

  //識別id
  private int id;
  //技の名前
  private String skillName;
  //ダメージ技
  private int damage;
  //回復技
  private int recovery;
  //命中率
  private int hit;
  //属性
  private int type;
  //状態異常
  private int status;

  /**
   * コンストラクタ
   * @param id
   * @param skillName
   * @param damage
   * @param recovery
   * @param hit
   * @param type
   * @param status
   */
  public Skill(int id, String skillName, int damage, int recovery, int hit, int type, int status) {
    this.id = id;
    this.skillName = skillName;
    this.damage = damage;
    this.recovery = recovery;
    this.hit = hit;
    this.type = type;
    this.status = status;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSkillName() {
    return skillName;
  }

  public void setSkillName(String skillName) {
    this.skillName = skillName;
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public int getRecovery() {
    return recovery;
  }

  public void setRecovery(int recovery) {
    this.recovery = recovery;
  }

  public int getHit() {
    return hit;
  }

  public void setHit(int hit) {
    this.hit = hit;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getStatus() { return status; }

  public void setStatus(int status) { this.status = status; }

  /**
   * skillの情報が入っているリストを返すメソッド
   * @return jsonファイルから読み込んだスキル全種
   */
  public static Map<Integer, Skill> createSkillInstance() {
    // jsonファイルの読み込み
    JsonNode root = Util.readJson("D:\\Pokemon2\\src\\main\\java\\skill.json", "Skill");

    Map<Integer, Skill> savedSkill = new HashMap<>();

    for (JsonNode JsonSkill : root) {
      // ポケモンインスタンス生成,パラメータは6こ
      Skill skills = new Skill(
              JsonSkill.get("id").asInt(),
              JsonSkill.get("name").asText(),
              JsonSkill.get("damage").asInt(),
              JsonSkill.get("recovery").asInt(),
              JsonSkill.get("hit").asInt(),
              JsonSkill.get("type").asInt(),
              JsonSkill.get("status").asInt());

      savedSkill.put(skills.getId(), skills);
    }
    // 技18個のリスト(インスタンス)
    return savedSkill;
  }
}

