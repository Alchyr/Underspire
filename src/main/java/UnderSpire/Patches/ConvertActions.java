package UnderSpire.Patches;

import UnderSpire.Interfaces.UndertalePlayer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CtBehavior;

import java.util.ArrayList;

import static UnderSpire.UnderSpire.generateEnemyAttack;

@SpirePatch(
        clz = GameActionManager.class,
        method = "getNextAction"
)
public class ConvertActions {
    public static ArrayList<AbstractGameAction> enemyTargetedActions = new ArrayList<>();

    @SpirePrefixPatch
    public static SpireReturn Prefix(GameActionManager __instance)
    {
        if (AbstractDungeon.player instanceof UndertalePlayer)
        {
            if (__instance.turnHasEnded)
            {
                if (!__instance.actions.isEmpty())
                {
                    AbstractGameAction nextAction = __instance.actions.get(0);
                    if (nextAction.target != null && nextAction.target.isPlayer)
                    {
                        if (nextAction.source instanceof AbstractMonster || nextAction.actionType == AbstractGameAction.ActionType.DAMAGE)
                        {
                            enemyTargetedActions.add(__instance.actions.remove(0));
                            return SpireReturn.Return(null);
                        }
                    }
                    else if (nextAction instanceof MakeTempCardInDiscardAction ||
                            nextAction instanceof MakeTempCardInDiscardAndDeckAction ||
                            nextAction instanceof MakeTempCardInHandAction ||
                            nextAction instanceof MakeTempCardInDrawPileAction ||
                            nextAction instanceof MakeTempCardAtBottomOfDeckAction)
                    {
                        __instance.actions.remove(0); //Report Debuff or something
                        //enemyTargetedActions.add();
                        return SpireReturn.Return(null);
                    }
                    else if (nextAction instanceof AddCardToDeckAction)
                    {
                        __instance.actions.remove(0); //report PermaDebuff or something
                        //enemyTargetedActions.add();
                        return SpireReturn.Return(null);
                    }
                }
            }
        }
        return SpireReturn.Continue();
    }

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn Insert(GameActionManager __instance)
    {
        //This is when all monsters have queued their actions and there are no actions left in queue

        //With previous patch, any actions targeting player should have been stored in the arraylist.
        //Now, pass them to UnderSpire and add a new action to queue which will perform fancy enemy attack.

        if (!enemyTargetedActions.isEmpty())
        {
            generateEnemyAttack(enemyTargetedActions);
            enemyTargetedActions.clear();
            return SpireReturn.Return(null);
        }

        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "applyEndOfTurnPowers");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
