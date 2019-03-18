package UnderSpire.Actions.General;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FlashHandAction extends AbstractGameAction {
    @Override
    public void update() {
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            c.flash();
        }

        this.isDone = true;
    }
}
