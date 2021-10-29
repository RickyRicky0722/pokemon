
import java.util.*;

public class Battle {
  //トレーナー情報
  private Trainer trainer1;
  private Trainer trainer2;
  //バトルで使われるポケモン
  private Pokemon pokemon1;
  private Pokemon pokemon2;
  //ポケモンのスキルmap
  private Map<Integer, Skill> skillMap1 = new HashMap<>();
  private Map<Integer, Skill> skillMap2 = new HashMap<>();

  /**
   * コンストラクタ
   */
  public Battle(Trainer trainer1, Trainer trainer2, Map<Integer, Skill> SkillMap1, Map<Integer, Skill> SkillMap2) {

    this.trainer1 = trainer1;
    this.trainer2 = trainer2;
    this.pokemon1 = trainer1.getStockPokemon().get(trainer1.getFirstPokemonId());
    this.pokemon2 = trainer2.getStockPokemon().get(trainer2.getFirstPokemonId());
    this.skillMap1 = new HashMap<>();
    this.skillMap2 = new HashMap<>();

  }

  public Trainer getTrainer1() {
    return trainer1;
  }

  public void setTrainer1(Trainer trainer1) {
    this.trainer1 = trainer1;
  }

  public Trainer getTrainer2() {
    return trainer2;
  }

  public void setTrainer2(Trainer trainer2) {
    this.trainer2 = trainer2;
  }

  public Pokemon getPokemon1() {
    return pokemon1;
  }

  public void setPokemon1(Pokemon pokemon1) {
    this.pokemon1 = pokemon1;
  }

  public Pokemon getPokemon2() {
    return pokemon2;
  }

  public void setPokemon2(Pokemon pokemon2) {
    this.pokemon2 = pokemon2;
  }


  public Map<Integer, Skill> getSkillMap1() {
    return skillMap1;
  }

  public void setSkillMap1(Map<Integer, Skill> skillMap1) {
    this.skillMap1 = skillMap1;
  }

  public Map<Integer, Skill> getSkillMap2() {
    return skillMap2;
  }

  public void setSkillMap2(Map<Integer, Skill> skillMap2) {
    this.skillMap2 = skillMap2;
  }

  /**
   * バトル開始メソッド
   * @param trainer1 一人目のトレーナー
   * @param trainer2 二人目のトレーナー
   */
  public void startBattle(Trainer trainer1, Trainer trainer2) {

    System.out.println("[バトルかいし！]");
    System.out.println(trainer1.getName() + "VS" + trainer2.getName());
    sleep2Second();

    setPokemon1(selectBattlePokemon(trainer1)); // Pokemonインスタンスを返す
    setPokemon2(selectBattlePokemon(trainer2)); // Pokemonインスタンスを返す

    while (true)
    {
      //一人目のターン開始
      System.out.println(pokemon1.getPokemonName() + "はどうする？");
      showSkillBox(pokemon1);//ポケモンの技の選択肢を表示する（逃げるも含む）
      setSkillMap1(pokemon1.getSkill()); //選択した技を保存
      int getSkill1 = skillChoice(skillMap1); //スキルIDを保存
      //逃げるを選択したかどうかの判定
      if(getSkill1 == 0) {
        runAway(pokemon1, trainer2); //相手の勝利
      }
      //HPの計算
      calcHp(pokemon1, pokemon2, getSkill1);
      //勝者が決まったら、ループを抜ける
      if (isDecideWinner(trainer2, trainer1, pokemon2)) break;

      //ポケモンのHPが0かどうか
      if (pokemon2.getHp() <= 0) setPokemon2(selectBattlePokemon(trainer2));

      //ポケモンの現在の体力、状態異常を表示
      //System.out.println(trainer1.getName() + "の" + pokemon1.getPokemonName() + "の残りHP：" + pokemon1.getHp());
      //System.out.println(trainer2.getName() + "の" + pokemon2.getPokemonName() + "の残りHP：" + pokemon2.getHp());
      //Status.nowCondition(pokemon1);
      //Status.nowCondition(pokemon2);

      //2人目のターン
      System.out.println(pokemon2.getPokemonName() + "はどうする？");
      showSkillBox(pokemon2);//ポケモンの技の選択肢を表示する（逃げるも含む）
      setSkillMap2(pokemon2.getSkill());//選択した技を保存
      int getSkill2 = skillChoice(skillMap2);//スキルIDを保存
      //逃げるを選択したかどうかの判定
      if(getSkill2 == 0) {
        //相手の勝利
        runAway(pokemon2, trainer1);
      }
      //hpの計算
      calcHp(pokemon2, pokemon1, getSkill2);
      //勝者判定
      if (isDecideWinner(trainer1, trainer2, pokemon1)) break;
      //他の手持ちのポケモンを選択
      if (pokemon1.getHp() <= 0) setPokemon1(selectBattlePokemon(trainer1));

      Status status = new Status();
      //どく、やけどを背負っている場合、10ダメージ
      status.effectStatusDamage(pokemon1);
      //勝者判定
      if (isDecideWinner(trainer1, trainer2, pokemon1)) break;
      //他の手持ちのポケモンを選択
      if (pokemon1.getHp() <= 0) setPokemon1(selectBattlePokemon(trainer1));
      //どく、やけどを背負っている場合、10ダメージ
      status.effectStatusDamage(pokemon2);
      //勝者判定
      if (isDecideWinner(trainer2, trainer1, pokemon2)) break;
      //他の手持ちのポケモンを選択
      if (pokemon2.getHp() <= 0) setPokemon2(selectBattlePokemon(trainer2));

      //ポケモンの現在の体力、状態異常を表示
      System.out.println(trainer1.getName() + "の" + pokemon1.getPokemonName() + "の残りHP：" + pokemon1.getHp());
      System.out.println(trainer2.getName() + "の" + pokemon2.getPokemonName() + "の残りHP：" + pokemon2.getHp());
      Status.nowCondition(pokemon1);
      Status.nowCondition(pokemon2);
    }
  }

  /**
   * 勝者判定メソッド
   * @param loseTrainer 敗者
   * @param winTrainer 勝者
   * @param defender やられているポケモン
   */
  public boolean isDecideWinner(Trainer loseTrainer, Trainer winTrainer, Pokemon defender)
  {
    //相手のポケモンのhpが0かどうか
    if(defender.getHp() <= 0)
    {
      System.out.println(defender.getPokemonName() + "のHPが0になってしまった！！");
      //手持ちのポケモンが0かどうか
      if (loseTrainer.getStockPokemon().size() == 0)
      {
        System.out.println(loseTrainer.getName() + "の手持ちのポケモンがいなくなってしまった！！");
        System.out.println("勝者：" + winTrainer.getName());
        return true;
      }
    }
    return false;
  }

  /**
   *
   * @param trainer ポケモンを戦わせるトレーナー
   * @return 手持ちから選択されたポケモン
   */
  public Pokemon selectBattlePokemon(Trainer trainer) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("\n" + trainer.getName() + "、どのポケモンをバトルにつかいますか？idでえらんでください\n" +
            "------------------------------------------------------------------------");
    //すべての手持ちポケモンを出力する
    for (Map.Entry<Integer, Pokemon> entry : trainer.getStockPokemon().entrySet()) {
      System.out.println(String.format("id:" + "%02d", entry.getKey()) + "  " + entry.getValue().getPokemonName());
    }
    System.out.print("id:");
    int pokemonId = 0;
    boolean loop = true;
    while (loop) {

      try {
        //入力されたポケモンIDを読み取る
        pokemonId = scanner.nextInt();
        if(trainer.getStockPokemon().get(pokemonId) == null) {
          System.out.print("てもちのポケモンからえらんでください\nid:");
        } else {
          loop = false;
        }
      }catch (InputMismatchException e) {
        System.out.print("数字でえらんでください\nid:");

        scanner.next();
      }
    }

    Pokemon battlePokemon = trainer.getStockPokemon().get(pokemonId);
    System.out.println("ゆけっ" + battlePokemon.getPokemonName() + "\n");
    Battle.sleep1Second();
    //手持ちのポケモンから削除する
    //TODO: 手持ちから削除するタイミングの変更(途中交代の実装)
    trainer.getStockPokemon().remove(pokemonId);
    return battlePokemon;

  }

  /**
   *
   * @param skillMap ポケモンが所持している技
   * @return 選択した技のID
   */
  public static int skillChoice(Map<Integer, Skill> skillMap) {
    Scanner scanner = new Scanner(System.in);
    int skill = 0;

    boolean judgeLoop = true;
    while(judgeLoop) {

      try {
        skill = scanner.nextInt();
      if(!(skillMap.containsKey(skill) || skill == 0)) {
        System.out.println("せんたくしのなかからえらんでください\nid:");
      }else {
        judgeLoop = false;
      }
      }catch (InputMismatchException e) {
        System.out.print("数字でえらんでください\nid:");
        scanner.next();
      }
    }

    return skill;
  }

  /**
   * ユーザーが逃げるを選択した場合のメソッド
   * @param pokemon 逃げるを選択したポケモン
   * @param winner 勝利したトレーナー
   */
  public static void runAway(Pokemon pokemon, Trainer winner) {
    System.out.println(pokemon.getPokemonName() + "は逃げだした\n");

    sleep4Second();
    System.out.println("しょうしゃ" + winner.getName() + "!");

    System.exit(0);
  }

  /**
   * HPの計算
   * @param attacker 攻撃ポケモン
   * @param defender 守備ポケモン
   * @param pokemonSkill 攻撃ポケモンの選択した技
   */
  public void calcHp(Pokemon attacker, Pokemon defender, int pokemonSkill){
    Skill skill = attacker.getSkill().get(pokemonSkill); //選択した技の取得
    int hitRate = skill.getHit(); //命中率
    if(pokemonSkill == 0) return; //逃げるではダメージは与えられない
    Status status = new Status();
    status.restoreRandomStatus(attacker); //状態異常ターン経過回復

    //眠っている場合、行動できない
    if (attacker.getStatus() == Status.SLEEP)
    {
      System.out.println(attacker.getPokemonName() + "は眠っている！！");
      return;
    }

    //状態異常による行動不可判定
    if(!status.isHitSuccess(attacker.getStatus()))
    {
      String text = "状態異常";
      if (attacker.getStatus() == Status.PARALYSIS) { text = Status.STRING_PARALYSIS; }
      else if (attacker.getStatus() == Status.CONFUSION) { text = Status.STRING_CONFUSION; }

      System.out.println(attacker.getPokemonName() + "は" + text + "によって身動きがとれない！！！");
      return;
    }

    //命中率の計算
    if (!isHit(hitRate))
    {
      //このターンはアクションなし
      System.out.println(attacker.getPokemonName() + "は技の発動を失敗してしまった！！");
      return;
    }

    //自滅ダメージかどうか
    if (status.isSelfDestruct(attacker.getStatus()))
    {
      System.out.println(attacker.getPokemonName() + "の攻撃！！ " + skill.getSkillName());
      System.out.println(attacker.getPokemonName() + "は自分に攻撃してしまった！！！");
      System.out.println(attacker.getPokemonName() + "に20ダメージ！！！");
      //混乱は20ダメージ固定
      attacker.setHp(attacker.getHp() - 20);
      attacker.setStatus(Status.FINE);
      System.out.println(attacker.getPokemonName() + "は自分の攻撃で我に返った！！！");
      return;
    }

    int afterHp;
    //攻撃技のみ
    if(skill.getDamage() > 0 && skill.getRecovery() == 0)
    {
      Attribute attr = new Attribute();
      //TODO: タイプ一致計算？
      double damageRate =  attr.calcAttributeRate(skill.getType(), defender.getAttribute());
      int thisTimeDamage =  (int)(damageRate *  skill.getDamage());
      //やけどにより攻撃力低下
      if (attacker.getStatus() == Status.BURN) thisTimeDamage = thisTimeDamage - Status.BURN_DAMAGE;
      //最低でも5ダメージは与えるようにする
      if (thisTimeDamage < 5) thisTimeDamage = 5;

        //ダメージ計算
        afterHp = defender.getHp() - thisTimeDamage;
        defender.setHp(afterHp);

        System.out.println(attacker.getPokemonName() + "の攻撃！！ " + skill.getSkillName());
        if (damageRate == 0)
        {
          System.out.println(defender.getPokemonName() + "には効果はないようだ・・・");
          return;
        }
        else if (damageRate > 1)
        {
          System.out.println("効果は抜群だ！！！");
        }
        else if (damageRate < 1)
        {
          System.out.println("効果は今一つだ・・・");
        }
        System.out.println(defender.getPokemonName() + "に" + thisTimeDamage + "ダメージ！！");
        if (defender.getHp() <= 0) defender.setHp(0); //マイナスHPは存在しないため

      //状態異常を相手ポケモンに付与する
      status.judgeStatus(skill, defender);

    }
    else if (skill.getRecovery() > 0 && skill.getDamage() == 0) //回復のみ
    {
      //HP回復
      afterHp = attacker.getHp() + skill.getRecovery();
      attacker.setHp(afterHp);

      System.out.println(attacker.getPokemonName() + "は" + skill.getSkillName() + "を使った！！");
      System.out.println(attacker.getPokemonName() + "はHPを" + skill.getRecovery() + "回復した！！");

      //上限値以上は回復しない
      if (attacker.getHp() > attacker.getMaxHp())
      {
        //上限値代入
        attacker.setHp(attacker.getMaxHp());
        System.out.println(attacker.getPokemonName() + "の最大HPは" + attacker.getMaxHp() + "なのでこれ以上は回復しない！！");
      }
    }
    else if (skill.getRecovery() == 0 && skill.getDamage() == 0) //状態異常技
    {
      System.out.println(attacker.getPokemonName() + "の攻撃！！ " + skill.getSkillName());
      //状態異常を相手ポケモンに付与する
      status.judgeStatus(skill, defender);
    }
    //TODO: HP吸収技実装? (skill.getRecovery() != 0 && skill.getDamage() != 0)
  }

  /**
   * 命中率を取得し、計算する
   * @param hitRate 命中率
   * @return 命中可否
   */
  public static boolean isHit(int hitRate){
      Random random = new Random();
      int randomValue = random.nextInt(101);
      return randomValue <= hitRate;
  }

  /**
   * ポケモンが所持している技の一覧を表示する
   * @param pokemon 技の一覧を表示するポケモン
   */
  public static void showSkillBox(Pokemon pokemon) {

    Map<Integer, Skill> pokemonSkills = pokemon.getSkill();

    //ポケモンの技をすべて表示する
    for(Map.Entry<Integer, Skill> entry : pokemonSkills.entrySet()) {
      System.out.println("-------------------------\n"
              + entry.getKey() + " : " + entry.getValue().getSkillName() + "\n"
              + "ダメージ : " + entry.getValue().getDamage() + "\n"
              + "かいふく : " + entry.getValue().getRecovery());
    }
    System.out.println("------------------------- \n00 : にげる\n-------------------------");
  }


  /**
   * 1秒間の停止メソッド
   */
  private static void sleep1Second() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * 2秒間停止のメソッド
   */
  private static void sleep2Second() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * 4秒間停止メソッド
   */
  private static void sleep4Second() {
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
