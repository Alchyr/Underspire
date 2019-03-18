package UnderSpire.Patches;

import UnderSpire.Interfaces.UndertalePlayer;
import UnderSpire.Relics.YourSoul;
import UnderSpire.Rewards.ExpReward;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

public class ModifyRewards {
    @SpirePatch(
            clz = AbstractRoom.class,
            method = "update"
    )
    public static class AddRewards
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractRoom __instance)
        {

        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "addPotionToRewards");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "addPotionToRewards",
            paramtypez = {}
    )
    public static class NoPotions
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractRoom __instance)
        {
            if (AbstractDungeon.player instanceof UndertalePlayer)
            {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
