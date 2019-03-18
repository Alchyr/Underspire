/*package UnderSpire.Patches;

import UnderSpire.Interfaces.UndertalePlayer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

@SpirePatch(
        clz = EnergyPanel.class,
        method = "render"
)
public class NoEnergyNumber {
    @SpirePrefixPatch
    public static SpireReturn NoRender(EnergyPanel __instance, SpriteBatch sb)
    {
        if (AbstractDungeon.player instanceof UndertalePlayer)
        {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
*/