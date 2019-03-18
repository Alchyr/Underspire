package UnderSpire.Patches;


import UnderSpire.Interfaces.UndertalePlayer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = CardGroup.class,
        method = "initializeDeck"
)
public class InitializeUndertaleDeck {
    @SpirePrefixPatch
    public static SpireReturn GetUndertaleDeck(CardGroup __instance, CardGroup masterDeck)
    {
        if (AbstractDungeon.player instanceof UndertalePlayer)
        {
            __instance.clear();

            //remove all cards.

            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
