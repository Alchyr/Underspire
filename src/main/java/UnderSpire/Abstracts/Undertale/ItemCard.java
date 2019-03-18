package UnderSpire.Abstracts.Undertale;

import UnderSpire.Actions.Undertale.TextBoxAction;
import UnderSpire.Actions.Undertale.WaitForTextDoneAction;
import UnderSpire.Patches.InventoryGroup;
import UnderSpire.Util.CardInfo;
import UnderSpire.Util.UndertaleText;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class ItemCard extends BaseUndertaleCard {
    public ItemCard(CardInfo cardInfo)
    {
        super(cardInfo);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        InventoryGroup.Inventory.get(p).removeCard(this);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
    public void upgrade() {}

    public void addDefaultTextActions()
    {
        AbstractDungeon.actionManager.addToBottom(new TextBoxAction(getUseText()));
        AbstractDungeon.actionManager.addToBottom(new WaitForTextDoneAction());
    }

    public UndertaleText getUseText() //use extended description to store things, and some cards do special stuff
    {
        return new UndertaleText(cardStrings.EXTENDED_DESCRIPTION, 0.035f);
    }

    @Override
    public void render(SpriteBatch sb, boolean selected) {
        super.render(sb, selected);
    }
}
