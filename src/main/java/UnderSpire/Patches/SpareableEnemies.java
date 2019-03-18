package UnderSpire.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        clz = AbstractMonster.class,
        method = SpirePatch.CLASS
)
public class SpareableEnemies {
    public static SpireField<Boolean> Spareable = new SpireField<>(()->false);
}
