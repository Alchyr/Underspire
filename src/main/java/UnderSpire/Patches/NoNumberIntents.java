package UnderSpire.Patches;

import UnderSpire.Interfaces.UndertalePlayer;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CtBehavior;

import java.util.ArrayList;

public class NoNumberIntents
{
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "renderDamageRange"
    )
    public static class NoNumber {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractMonster __instance)
        {
            if (AbstractDungeon.player instanceof UndertalePlayer)
            {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractMonster.class,
            method = "renderTip"
    )
    public static class NoTip {
        @SpireInsertPatch (
                    locator = Locator.class,
                    localvars = { "tips" }
            )
        public static void Insert(AbstractMonster __instance, SpriteBatch sb, ArrayList<PowerTip> tips)
        {
            if (AbstractDungeon.player instanceof UndertalePlayer)
            {
                PowerTip intentTip = (PowerTip) ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "intentTip");
                //it's fine if it's null.
                tips.remove(intentTip);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
