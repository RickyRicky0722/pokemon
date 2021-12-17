import java.util.HashMap;
import java.util.Map;

public class Main {

  public static void main(String[] args) {

    // 1人目のトレーナー情報
    // トレーナーの名前と性別
    Trainer trainer1 = new Trainer("takeshi", "男", new HashMap<Integer, Pokemon>(), new HashMap<Integer, Pokemon>());
    System.out.println("ひとりめのじょうほうを入力してください");

    trainer1.createPokemonInstance();
    trainer1.setTrainerInfo(); // 名前と性別の設定
    trainer1.showPokemonBox(); // ポケモンの一覧を表示
    trainer1.chosePokemon(); // 手持ちポケモンを設定


    Trainer trainer2 = new Trainer("Hikari", "女", new HashMap<Integer, Pokemon>(), new HashMap<Integer, Pokemon>());
    System.out.println("ふたりめのじょうほうをにゅうりょくしてください");
    trainer2.createPokemonInstance();
    trainer2.setTrainerInfo();
    trainer2.showPokemonBox();
    trainer2.chosePokemon();

    Battle battle = new Battle(trainer1, trainer2, new HashMap<Integer, Skill>(), new HashMap<Integer, Skill>());

    //バトル開始
    battle.startBattle(trainer1, trainer2);

    //システム終了
    System.exit(0);

  }
}
