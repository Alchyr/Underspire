package UnderSpire.Actions.Undertale;

import UnderSpire.Util.Sounds;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class LevelUpAction extends AbstractGameAction {
    private static final float DURATION = 0.25f;

    private boolean showEffects;

    public LevelUpAction(int amount, boolean showEffects)
    {
        this.amount = amount;
        this.showEffects = showEffects;
        this.actionType = ActionType.DAMAGE; //prevents cancel by end of combat... moderately janky?

        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (this.duration == DURATION)
        {
            int hpAddition = 4;
            if (amount == 20 && AbstractDungeon.player.maxHealth < 99)
            {
                hpAddition = 99 - AbstractDungeon.player.maxHealth;
            }

            AbstractDungeon.player.increaseMaxHp(hpAddition, showEffects);

            CardCrawlGame.sound.play(Sounds.LEVEL_UP.getKey());
        }

        this.tickDuration();
    }
}