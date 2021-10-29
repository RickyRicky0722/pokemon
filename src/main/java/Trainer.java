
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.IDN;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * トレーナークラス
 */
public class Trainer {
  // 所持できるポケモンの数
  private final int LIMIT_STOCK_POKEMON = 6;

  // 名前
  private String name;
  // 性別
  private String gender;
  // ポケモンボックス（全てのポケモン）

  private Map<Integer, Pokemon> pokemonBox;

  //　手もちポケモン（トレーナーのチョイスしたポケモン）　
  private Map<Integer, Pokemon> stockPokemon;
  // 手持ちポケモンの最初のポケモン
  private int firstPokemonId;

  /**
   * コンストラタ
   */
  public Trainer(String name, String gender, Map<Integer, Pokemon> pokemonBox, Map<Integer, Pokemon> stockPokemon) {
    this.name = name;
    this.gender = gender;
    this.pokemonBox = new HashMap<>();
    this.stockPokemon = new HashMap<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<Integer, Pokemon> getPokemonBox() {
    return pokemonBox;
  }

  public void setPokemonBox(Map<Integer, Pokemon> pokemonBox) {
    this.pokemonBox = pokemonBox;
  }

  public Map<Integer, Pokemon> getStockPokemon() {
    return stockPokemon;
  }

  public void setStockPokemon(Map<Integer, Pokemon> stockPokemon) {
    this.stockPokemon = stockPokemon;
  }

  public int getFirstPokemonId() {
    return firstPokemonId;
  }

  public void setFirstPokemonId(int firstPokemonId) {
    this.firstPokemonId = firstPokemonId;
  }

  /**
   * インスタンス情報を入力するメソッド（名前と性別）
   */

  public void setTrainerInfo() {
    Scanner scanner = new Scanner(System.in);
    boolean isNameLoop = true;
    while (isNameLoop) {

      System.out.print("トレーナーのなまえをにゅうりょくしてください：");
      name = scanner.nextLine();//名前を入力
      boolean isJudgeNameLoop = true;

      while (isJudgeNameLoop) {
        System.out.print("きみのなまえは" + name + "かな？\nはんかくすうじでえらんでください(1:はい 2:いいえ)");
        String judgeName = scanner.nextLine();
        if (judgeName.equals("1")) {
          isNameLoop = false;
          isJudgeNameLoop = false;
        } else if (judgeName.equals("2")) {
          isJudgeNameLoop = false;
        } else {
          System.out.println("1または2をえらんでください");
        }
      }
    }

    boolean isLoop = true;
    String error = " ";
    while (isLoop) {
      System.out.print("せいべつをすうじでえらんでください（1:おとこ 2:おんな）");

      gender = scanner.next();//性別を入力
      // 意図しない数字が入力された場合の判定
      if (!(gender.equals("1") || (gender.equals("2")))) {
        System.out.println("1または2をえらんでください");
      } else {
        isLoop = false;
      }
    }

    String outGender = " ";
    if (gender.equals("1")) {
      outGender = "おとこ";
    } else {
      outGender = "おんな";

    }
    System.out.println("なまえ：" + name);
    System.out.println("せいべつ：" + outGender);
  }

  /**
   * ポケモンごとのインスタンスを作成して配列で返す
   */
  public void createPokemonInstance() {

    // pokemonのJSONファイルを読み込む
    //JsonNode root = Util.readJson("D:\\Workspace\\Pokemon2\\src\\main\\java\\pokemon.json", "Pokemon");
    JsonNode root = Util.readJson("D:\\Pokemon2\\src\\main\\java\\pokemon.json", "Pokemon");

    if (root == null) {
      return;
    }
    // 読み取った内容を用いてpokemonインスタンスの配列を作成する
    Map<Integer, Pokemon> pokemonInstance = new HashMap<>();
    Map<Integer, Skill> pokemonSkills = Skill.createSkillInstance();

    //TODO:pokemonSkillsの空判定追加

    for (JsonNode jsonNode : root) {
      // ポケモンインスタンス生成
      JsonNode skillNode = jsonNode.get("skillId");
      Stream<JsonNode> streamNode = StreamSupport.stream(skillNode.spliterator(), false);
//      .map(e -> {return e.asText()})
      List<String> skillList = streamNode.map(JsonNode::asText).collect(Collectors.toList());
      Map<Integer, Skill> skillMap = new HashMap<>();

      //中の要素分回す
      //最大4回 回す
      for (String skills: skillList) {
        int skill = Integer.parseInt(skills);
        //最大18回 回す

        skillMap.put(skill,pokemonSkills.get(skill));
        //System.out.println(skills + pokemonSkills.get(skill).getSkillName());
//        pokemonSkills.forEach((key, value) -> {
//          if(key == skill) {
//            skillMap.put(key, value);
//            System.out.println(skills + value.getSkillName());
//          }
//        });
      }
      //要素数5つ
      Pokemon pokemon = new Pokemon(
              jsonNode.get("id").asInt(),
              jsonNode.get("pokemonName").asText(),
              jsonNode.get("attribute").asInt(),
              jsonNode.get("gender").asText(),
              jsonNode.get("hp").asInt(),
              skillMap,
              jsonNode.get("maxHp").asInt(),
              Status.FINE,
              0
              // TODO: 2021/09/27 Skill インスタンスのMapを入れる
      );
      pokemonInstance.put(pokemon.getId(), pokemon);
    }
    setPokemonBox(pokemonInstance);
  }

  /**
   * ポケモンのIDと名前の一覧を出力する
   */
  public void showPokemonBox() {
    System.out.println("---------------------------------");
    System.out.println("【ポケモンボックス】");
    for (Map.Entry<Integer, Pokemon> entry : pokemonBox.entrySet()) {
      System.out.println(String.format("%02d", entry.getKey()) + " : " + entry.getValue().getPokemonName());
    }
  }

  /**
   * 手持ちポケモンを選択する
   */
  public void chosePokemon() {
    //stockPokemonの入れ物のなる変数を定義　
    Map<Integer, Pokemon> tmpStockPokemon = new HashMap<>();

    Scanner scanner = new Scanner(System.in);
    // 目的：pokemons に手持ちのポケモン(インスタンス)を入れる
    // 方法: ユーザーがえらんだポケモンを入れる　(idをユーザーが選択)
    // 最大6体まで選択
    // 取ってくるポケモンの情報はpokemonBoxに入ってる

    System.out.print("てもちポケモンをきめましょう。ポケモンボックスの中からIDでえらんでください。\nえらべるポケモンのかずは" + LIMIT_STOCK_POKEMON + "たいまでです。\n");

    for (int i = 1; i <= LIMIT_STOCK_POKEMON; i++) {
      int selectPokemonId;
      try {
        System.out.print("id :");
        selectPokemonId = scanner.nextInt(); //id番号が入力値　（ローカル変数pokemonsに入れる）
        // ポケモンボックスの中にあるポケモンだけ選択可能
        if (pokemonBox.get(selectPokemonId) == null) {
          System.out.println("ポケモンボックスのなかからえらんでください");
          i -= 1;
          continue;
        }
      } catch (InputMismatchException e) {
        System.out.println("すうじでえらんでください");
        scanner.next();
        i -= 1;
        continue;
      }
      //　重複の判定
      if (tmpStockPokemon.containsKey(selectPokemonId)) {
        System.out.println("おなじにゅうりょくがありました。えらびなおしてください。");
        i -= 1;
        continue;
      }
      // 入力された一番最初のポケモンのidをセットする
      if(i == 1) setFirstPokemonId(selectPokemonId);

      tmpStockPokemon.put(selectPokemonId, pokemonBox.get(selectPokemonId)); //　判定済みのポケモンを格納
      //　stockPokemonに手持ちポケモンにするインスタンスを入れておく
      //boolean isLoop = true;
      boolean isLoop = i < LIMIT_STOCK_POKEMON;
      while (isLoop) {
        System.out.print("もう1たいえらびますか？(1:はい 2:いいえ)");
        try {
          int loop = scanner.nextInt();
          if (loop == 1) {
            isLoop = false;
          } else if (loop == 2) {
            isLoop = false;
            i = 7;
          } else {
            System.out.println("1 または 2 でえらんでください");
          }
        } catch (InputMismatchException e) {
          System.out.println("すうじでえらんでください");
          scanner.next();
          isLoop = true;
        }
      }
    }
    setStockPokemon(tmpStockPokemon);


//    Map<String, String> obj = new HashMap<>();
//    obj.put("name", "加藤　力也");
//    obj.get("name");
//
//    Map<Integer, Pokemon> pokemons = new HashMap<>();
//    pokemons.put(1, new Pokemon(1,"tanaka", " ", "まつこ",100, new ArrayList<>() ));
//
//    pokemons.get(1);


  }
}




