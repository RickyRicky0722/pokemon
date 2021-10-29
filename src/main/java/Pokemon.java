
import java.util.HashMap;
import java.util.Map;

public class Pokemon {

  private int id;
  //名前
  private String pokemonName;
  //属性
  private int attribute;
  //性別
  private String gender;
  //ヒットポイント
  private int hp;
  // skillオブジェクトのマップ
  private Map<Integer, Skill> skill;
  //最大HP
  private int maxHp;
  //状態
  private int status;
  //状態異常カウント
  private int statusCount;

  //技(idの配列)
  //private List<String> skill = new ArrayList<String>();

  /**
   * コンストラクタ
   *
   * @param id
   * @param pokemonName
   * @param attribute
   * @param gender
   * @param hp
   * @param skill
   * @param maxHp
   * @param status
   * @param statusCount
   */

  public Pokemon(int id, String pokemonName, int attribute, String gender, int hp, Map<Integer, Skill> skill, int maxHp, int status, int statusCount) {
    this.id = id;
    this.pokemonName = pokemonName;
    this.attribute = attribute;
    this.gender = gender;
    this.hp = hp;
    this.skill = skill;
    this.maxHp = maxHp;
    this.status = status;
    this.statusCount = statusCount;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPokemonName() {
    return pokemonName;
  }

  public void setPokemonName(String pokemonName) {
    this.pokemonName = pokemonName;
  }

  public int getAttribute() {
    return attribute;
  }

  public void setAttribute(int attribute) {
    this.attribute = attribute;
  }

  public String getGender() {
    return gender;
  }

  public void setGender() { this.gender = gender; }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public Map<Integer, Skill> getSkill() {
    return skill;
  }

  public void setSkill(Map<Integer, Skill> skill) {
    this.skill = skill;
  }

  public int getMaxHp() { return maxHp; }

  public void setMaxHp(int maxHp) { this.maxHp = maxHp; }

  public int getStatus() { return status; }

  public void setStatus(int status) { this.status = status; }

  public int getStatusCount() { return statusCount; }

  public void setStatusCount(int statusCount) { this.statusCount = statusCount; }
}


