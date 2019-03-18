package UnderSpire.Patches;

import UnderSpire.Interfaces.UndertalePlayer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;

public class EndTurnButtonPatches {
    @SpirePatch(
            clz = EndTurnButton.class,
            method = "update"
    )
    public static class DisableEndTurn
    {
        @SpirePrefixPatch
        public static void Prefix(EndTurnButton __instance)
        {
            if (AbstractDungeon.player instanceof UndertalePlayer)
            {
                __instance.disable();
            }
        }
    }
}
