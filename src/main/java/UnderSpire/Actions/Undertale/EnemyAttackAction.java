package UnderSpire.Actions.Undertale;

import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.UI.MinigameSoul;
import UnderSpire.Effects.EnemyAttackEffect;
import UnderSpire.UI.BattleArea;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import static UnderSpire.Util.Sounds.GAME_OVER;

public class EnemyAttackAction extends AbstractGameAction {
    private ArrayList<AttackPattern> attackPatterns;
    private BattleArea area;
    private MinigameSoul player;

    private boolean first;

    private EnemyAttackEffect effect;

    public EnemyAttackAction(BattleArea area, MinigameSoul player, ArrayList<AttackPattern> attackPatterns, float duration)
    {
        this.duration = duration;
        this.attackPatterns = attackPatterns;
        this.area = area;
        this.player = player;

        first = true;

        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (first)
        {
            first = false;

            effect = new EnemyAttackEffect(area, player, attackPatterns, duration);
            AbstractDungeon.effectList.add(effect);
        }
        else
        {
            if (effect.isDone && !this.isDone)
            {
                this.isDone = true;
                if (effect.deathState == EnemyAttackEffect.DyingState.DONE)
                {
                    AbstractDungeon.player.currentHealth = 0;
                    AbstractDungeon.player.healthBarUpdatedEvent();
                    AbstractDungeon.player.isDead = true;
                    AbstractDungeon.deathScreen = new UnderSpire.UI.UndertaleDeathScreen();
                }
            }
        }
    }
}
