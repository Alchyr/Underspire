package UnderSpire.Actions.General;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DestroyHandAction extends AbstractGameAction {
    @Override
    public void update() {
        AbstractDungeon.player.hand.clear();

        this.isDone = true;
    }
}
