package UnderSpire.Patches;

import UnderSpire.Abstracts.Undertale.ItemCard;
import UnderSpire.Interfaces.UndertalePlayer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;

public class InitializeItemCardGroup {
    //Patch into AbstractPlayer.preBattlePrep and call this method
    //Initialize a cardgroup stored in UndertalePlayer interface, tracking all items the player currently has
    //These will be displayed when the player uses "Item"
    //Base on initializeDeck from cardgroup

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class ItemCardInitialize
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance)
        {
            if (AbstractDungeon.player instanceof UndertalePlayer)
            {
                ArrayList<AbstractCard> items = new ArrayList<>();

                CardGroup copy = new CardGroup(__instance.masterDeck, CardGroup.CardGroupType.UNSPECIFIED);

                InventoryGroup.Inventory.get(__instance).clear();

                for (AbstractCard c : copy.group)
                {
                    if (c instanceof ItemCard)
                    {
                        InventoryGroup.Inventory.get(__instance).addToTop(c);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "initializeDeck");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
