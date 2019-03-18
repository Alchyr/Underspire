package UnderSpire.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class UndertaleDeathScreen {
    @SpirePostfixPatch
    public static void setDeathScreen(AbstractPlayer __instance, DamageInfo info)
    {
        if (__instance.isDead)
        {
            AbstractDungeon.deathScreen = new UnderSpire.UI.UndertaleDeathScreen();
        }
    }
}
