import java.util.Random;

public class Status {
    //良好
    public static final int FINE = 0;
    //毒
    public static final int POISON = 1;
    //麻痺
    public static final int PARALYSIS = 2;
    //混乱
    public static final int CONFUSION = 3;
    //やけど
    public static final int BURN = 4;
    //ねむり
    public static final int SLEEP = 5;
    //どく(ひらがな)
    public static final String STRING_POISON = "どく";
    //まひ(ひらがな)
    public static final String STRING_PARALYSIS = "まひ";
    //こんらん(ひらがな)
    public static final String STRING_CONFUSION = "こんらん";
    //やけど(ひらがな)
    public static final String STRING_BURN = "やけど";
    //ねむり(ひらがな)
    public static final String STRING_SLEEP = "ねむり";
    //状態異常による攻撃ミス割合(何割)
    public static final int ATTACK_MISS_RATE = 2;
    //状態異常ダメージ技による状態異常付加率(何割)
    public static final int STATUS_ADD_RATE = 2;
    //やけどダメージ
    public static final int BURN_DAMAGE = 10;

    /**
     * 状態異常を付与するメソッド
     * @param skill 使用技
     * @param defender 攻撃を受けるポケモン
     */
    public void judgeStatus (Skill skill, Pokemon defender)
    {
        //状態異常技ではない、あるいはHPが0の場合、処理は行わない
        if (skill.getStatus() == FINE || defender.getHp() == 0) return;
        //毒技で鋼タイプに攻撃しても効果はないので判定しない
        if (skill.getStatus() == POISON && (defender.getAttribute() == Attribute.TYPE_STEEL || defender.getAttribute() == Attribute.TYPE_POISON) ||
        skill.getStatus() == BURN && defender.getAttribute() == Attribute.TYPE_FLAME) return;

        String text = "";

        //すでに状態異常かつ状態異常ダメージ技の場合
        if (skill.getDamage() > 0 && defender.getStatus() != FINE)
        {
            //状態異常判定を行わない
            return;
        }
        else if (skill.getDamage() == 0 && defender.getStatus() != FINE) //ポケモンが状態異常かつダメージなし状態異常攻撃の場合
        {
            //状態異常の重複はしない
            text = outputStatusText(defender.getStatus());
            System.out.println(defender.getPokemonName() + "はすでに" + text + "になっている！");
            return;
        }
        else if (skill.getDamage() > 0 && defender.getStatus() == FINE) //ダメージ技であるかどうか
        {
            //攻撃技の場合、30%の確率で状態異常を付与する
            Random r = new Random();
            if (r.nextInt(10) < STATUS_ADD_RATE) defender.setStatus(skill.getStatus());
            if (defender.getStatus() == FINE) return;
        }
        else if (skill.getDamage() == 0 && defender.getStatus() == FINE)
        {
            //ダメージ技でなければ必中
            defender.setStatus(skill.getStatus());
        }

        //状態異常になったことを出力
        text = outputStatusText(defender.getStatus());
        System.out.println(defender.getPokemonName() + "は" + text + "になってしまった！！！");
    }

    /**
     * 状態異常ターン経過回復メソッド
     * @param pokemon 状態異常回復を試みるポケモン
     */
    public void restoreRandomStatus (Pokemon pokemon)
    {
        //自然回復しない状態異常あるいは状態異常になったターンはターン経過による状態異常回復はしない
        if ((pokemon.getStatus() != BURN && pokemon.getStatus() != SLEEP)) return;
        Random rand = new Random();
        //状態異常になって次のターンは回復しない
        if(pokemon.getStatusCount() >= 1 && rand.nextInt(3) <= pokemon.getStatusCount() - 1)
        {
            String text;
            text =  pokemon.getStatus() == BURN ? STRING_BURN : STRING_SLEEP;
            System.out.println(text + "が治った！！");
            //状態異常回復
            pokemon.setStatus(FINE);
            pokemon.setStatusCount(0);
            return;
        }
        //状態異常カウントを+1する
        pokemon.setStatusCount(pokemon.getStatusCount() + 1);
    }

    /**
     * 状態異常による攻撃不可判定
     * @param status 状態異常
     */
    public boolean isHitSuccess(int status){
        //毒、やけどの場合は行動可能
        if (status != PARALYSIS && status != CONFUSION) return true;
        Random random = new Random();
        int randomValue = random.nextInt(10);
        return randomValue > ATTACK_MISS_RATE; //trueは行動成功、falseはこのターン行動不可
    }

    /**
     * 毒のダメージを与える
     * @param pokemon 毒ダメージを受けるポケモン
     */
    public void effectStatusDamage(Pokemon pokemon)
    {
        //毒あるいはやけど状態ならばターン終了時に10ダメージ
        if (pokemon.getStatus() == POISON || pokemon.getStatus() == BURN)
        {
            String text = "";
            pokemon.setHp(pokemon.getHp() - 10);
            text = pokemon.getStatus() == POISON ? STRING_POISON : STRING_BURN;
            System.out.println(pokemon.getPokemonName() + "は" + text + "により、10ダメージを受けた！！");
        }
    }

    /**
     * 混乱による自滅ダメージ(50%の確率で自滅)
     * @param status 状態異常
     * @return 自滅ダメージかどうか
     */
    public boolean isSelfDestruct (int status)
    {
        //混乱以外では自滅しないのでfalse
        if (status != CONFUSION) return false;
        Random r = new Random();
        int value = r.nextInt(2);
        return value == 1;
    }

    /**
     * ターン終了時に各ポケモンの状態を出力する
     * @param pokemon 状態を出力するポケモン
     */
    public static void nowCondition(Pokemon pokemon)
    {
        if (pokemon.getStatus() == POISON) { System.out.println(pokemon.getPokemonName() + "は毒にかかっている！！"); }
        else if (pokemon.getStatus() == PARALYSIS) { System.out.println(pokemon.getPokemonName() + "は麻痺でしびれている！！"); }
        else if (pokemon.getStatus() == CONFUSION) { System.out.println(pokemon.getPokemonName() + "は混乱している！！"); }
        else if (pokemon.getStatus() == BURN) { System.out.println(pokemon.getPokemonName() + "はやけどを背負っている！！"); }
        else if (pokemon.getStatus() == SLEEP) { System.out.println(pokemon.getPokemonName() + "は眠っている！！"); }
    }

    /**
     * 状態異常のテキスト化
     * @param status 状態異常ステータス
     * @return 状態異常のテキスト
     */
    public static String outputStatusText (int status)
    {
        String text = "";
        if (status == POISON) { text = STRING_POISON; }
        else if (status == PARALYSIS) { text = STRING_PARALYSIS; }
        else if (status == CONFUSION) { text = STRING_CONFUSION; }
        else if (status == BURN) { text = STRING_BURN; }
        else if (status == SLEEP) { text = STRING_SLEEP; }
        return text;
    }
}
