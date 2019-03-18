package UnderSpire.Actions.Undertale;

import UnderSpire.Actions.General.VFXWaitAction;
import UnderSpire.Effects.TextBoxEffect;
import UnderSpire.Interfaces.UndertalePlayer;
import UnderSpire.Util.UndertaleText;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static UnderSpire.UnderSpire.battleUI;

public class TextBoxAction extends AbstractGameAction {
    private UndertaleText text;

    public TextBoxAction(UndertaleText text)
    {
        this.text = text;
    }

    public void update()
    {
        if (AbstractDungeon.player instanceof UndertalePlayer)
            battleUI.setMessageText(text);
        else
        {
            TextBoxEffect textBoxEffect = new TextBoxEffect(text);
            AbstractDungeon.effectList.add(textBoxEffect);
            AbstractDungeon.actionManager.addToBottom(new VFXWaitAction(textBoxEffect));
        }

        this.isDone = true;
    }
}
