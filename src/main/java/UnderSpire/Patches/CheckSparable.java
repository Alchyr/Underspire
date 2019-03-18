package UnderSpire.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@SpirePatch(
        clz = AbstractMonster.class,
        method = "damage"
)
public class CheckSparable {
    @SpirePostfixPatch
    public static void CheckSparable(AbstractMonster __instance, DamageInfo info)
    {
        if (__instance.currentHealth > 0 && __instance.currentHealth < __instance.maxHealth / 4.5 && __instance.type != AbstractMonster.EnemyType.BOSS)
        {
            SpareableEnemies.Spareable.set(__instance, true);
        }
        else
        {
            SpareableEnemies.Spareable.set(__instance, false);
        }
    }
}
