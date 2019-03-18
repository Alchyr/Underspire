package UnderSpire.Actions.Undertale;

import UnderSpire.Interfaces.UndertalePlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static UnderSpire.UnderSpire.battleUI;

public class WaitForTextDoneAction extends AbstractGameAction {
    @Override
    public void update() {
        if (AbstractDungeon.player instanceof UndertalePlayer)
        {
            this.isDone = battleUI.textIsDone();
        }
        else
        {
            this.isDone = true;
        }
    }
}
