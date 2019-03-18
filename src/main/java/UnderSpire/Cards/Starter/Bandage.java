package UnderSpire.Cards.Starter;

import UnderSpire.Abstracts.Undertale.ItemCard;
import UnderSpire.Actions.Undertale.WaitForTextDoneAction;
import UnderSpire.Interfaces.ArmorCard;
import UnderSpire.Util.CardInfo;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static UnderSpire.UnderSpire.makeID;

public class Bandage extends ItemCard implements ArmorCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Bandage",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.BASIC
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int HEAL = 10;

    public Bandage()
    {
        super(cardInfo);
        FleetingField.fleeting.set(this, true);
        this.magicNumber = this.baseMagicNumber = HEAL;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        addDefaultTextActions();
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
    }

    @Override
    public int getDefense() {
        return 0;
    }
}