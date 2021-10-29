public class Attribute {

    //ノーマルタイプ
    public static final int TYPE_NORMAL = 0;
    //ほのおタイプ
    public static final int TYPE_FLAME = 1;
    //みずタイプ
    public static final int TYPE_WATER = 2;
    //でんきタイプ
    public static final int TYPE_ELECTRICITY = 3;
    //くさタイプ
    public static final int TYPE_GRASS = 4;
    //いわタイプ
    public static final int TYPE_ROCK = 5;
    //はがねタイプ
    public static final int TYPE_STEEL = 6;
    //どくタイプ
    public static final int TYPE_POISON = 7;

    //ノーマルタイプの攻撃で抜群
    public static final int[] NORMAL_EFFECTIVE = {};
    //ノーマルタイプの攻撃でいまひとつ
    public static final int[] NORMAL_SUBTLE = {TYPE_ROCK, TYPE_STEEL};
    //炎タイプの攻撃で抜群
    public static final int[] FLAME_EFFECTIVE = {TYPE_GRASS, TYPE_STEEL};
    //炎タイプでいまひとつ
    public static final int[] FLAME_SUBTLE = {TYPE_WATER, TYPE_ROCK, TYPE_FLAME};
    //水タイプの攻撃で抜群
    public static final int[] WATER_EFFECTIVE = {TYPE_FLAME, TYPE_ROCK};
    //水タイプでいまひとつ
    public static final int[] WATER_SUBTLE = {TYPE_GRASS, TYPE_WATER};
    //電気タイプの攻撃で抜群
    public static final int[] ELECTRICITY_EFFECTIVE = {TYPE_WATER};
    //電気タイプでいまひとつ
    public static final int[] ELECTRICITY_SUBTLE = {TYPE_ELECTRICITY};
    //草タイプの攻撃で抜群
    public static final int[] GRASS_EFFECTIVE = {TYPE_WATER, TYPE_ROCK};
    //草タイプでいまひとつ
    public static final int[] GRASS_SUBTLE = {TYPE_FLAME, TYPE_GRASS, TYPE_STEEL};
    //岩タイプの攻撃で抜群
    public static final int[] ROCK_EFFECTIVE = {TYPE_FLAME};
    //岩タイプでいまひとつ
    public static final int[] ROCK_SUBTLE = {TYPE_STEEL};
    //鋼タイプの攻撃で抜群
    public static final int[] STEEL_EFFECTIVE = {TYPE_ROCK};
    //鋼タイプでいまひとつ
    public static final int[] STEEL_SUBTLE = {TYPE_FLAME, TYPE_WATER, TYPE_ELECTRICITY, TYPE_STEEL};
    //どくタイプの攻撃で抜群
    public static final int[] POISON_EFFECTIVE = {TYPE_GRASS};
    //どくタイプでいまひとつ
    public static final int[] POISON_SUBTLE = {TYPE_POISON, TYPE_ROCK};
    //どくタイプで効果がない
    public static final int[] POISON_NODAMAGE = {TYPE_STEEL};


    //効かない(ダメージ0)
    public static final int NO_DAMAGE = 0;
    //効果は今一つ
    public static final int SUBTLE_DAMAGE = 1;
    //等倍
    public static final int NORMAL_DAMAGE = 2;
    //効果抜群
    public static final int CRITICAL_DAMAGE = 3;

    //効かない(ダメージ0)
    public static final double NO_DAMAGE_RATE = 0;
    //効果は今一つ
    public static final double SUBTLE_DAMAGE_RATE = 0.5;
    //等倍
    public static final double NORMAL_DAMAGE_RATE = 1.0;
    //効果抜群
    public static final double CRITICAL_DAMAGE_RATE = 1.5;

    /**
     * ダメージ倍率の計算
     * @param skillType 使用技のタイプ
     * @param defenceType　攻撃を受けるポケモンのタイプ
     * @return ダメージ倍率
     */
    public double calcAttributeRate(int skillType, int defenceType)
    {
        int[] effectiveList = {};
        int[] subtleList = {};
        int[] noDamageList = {};
        switch (skillType)
        {
            case TYPE_NORMAL:
                effectiveList = NORMAL_EFFECTIVE;
                subtleList = NORMAL_SUBTLE;
                break;
            case TYPE_FLAME:
                effectiveList = FLAME_EFFECTIVE;
                subtleList = FLAME_SUBTLE;
                break;
            case TYPE_WATER:
                effectiveList = WATER_EFFECTIVE;
                subtleList = WATER_SUBTLE;
                break;
            case TYPE_ELECTRICITY:
                effectiveList = ELECTRICITY_EFFECTIVE;
                subtleList = ELECTRICITY_SUBTLE;
                break;
            case TYPE_GRASS:
                effectiveList = GRASS_EFFECTIVE;
                subtleList = GRASS_SUBTLE;
                break;
            case TYPE_ROCK:
                effectiveList = ROCK_EFFECTIVE;
                subtleList = ROCK_SUBTLE;
                break;
            case TYPE_STEEL:
                effectiveList = STEEL_EFFECTIVE;
                subtleList = STEEL_SUBTLE;
                break;
            case TYPE_POISON:
                effectiveList = POISON_EFFECTIVE;
                subtleList = POISON_SUBTLE;
                noDamageList = POISON_NODAMAGE;
                break;
        }

        int damageRate = 99;

        for (int i : effectiveList)
        {
            if (i == defenceType)
            {
                damageRate = CRITICAL_DAMAGE;
                break;
            }
        }

        for (int j : subtleList)
        {
            if (j == defenceType)
            {
                damageRate = SUBTLE_DAMAGE;
                break;
            }
        }

        for (int k : noDamageList)
        {
            if (k == defenceType)
            {
                damageRate = NO_DAMAGE;
                break;
            }
        }

        if (damageRate == CRITICAL_DAMAGE) { return CRITICAL_DAMAGE_RATE; }
        else if (damageRate == SUBTLE_DAMAGE) { return SUBTLE_DAMAGE_RATE; }
        else if (damageRate == NO_DAMAGE) { return NO_DAMAGE_RATE; }
        else { return NORMAL_DAMAGE_RATE; }
    }
}
