package UnderSpire.Actions.General;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class VFXWaitAction extends AbstractGameAction {
    private AbstractGameEffect effect;
    private boolean isTopLevelEffect;
    private boolean firstRun;

    public VFXWaitAction(AbstractGameEffect effect) {
        this(null, effect, false);
    }

    public VFXWaitAction(AbstractCreature source, AbstractGameEffect effect) {
        this.setValues(source, source);
        this.effect = effect;
        this.actionType = ActionType.WAIT;
        this.isTopLevelEffect = false;
        firstRun = true;
    }

    public VFXWaitAction(AbstractCreature source, AbstractGameEffect effect, boolean topLevel) {
        this.setValues(source, source);
        this.effect = effect;
        this.actionType = ActionType.WAIT;
        this.isTopLevelEffect = topLevel;
        firstRun = true;
    }

    public void update() {
        if (firstRun) {
            if (this.isTopLevelEffect) {
                AbstractDungeon.topLevelEffects.add(this.effect);
            } else {
                AbstractDungeon.effectList.add(this.effect);
            }
            firstRun = false;
        }

        if (this.effect.isDone)
        {
            this.isDone = true;
        }
    }
}