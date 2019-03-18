package UnderSpire.Cards.Items;

import UnderSpire.Abstracts.Undertale.ItemCard;
import UnderSpire.Util.CardInfo;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static UnderSpire.UnderSpire.makeID;

public class PumpkinRings extends ItemCard {
    private final static CardInfo cardInfo = new CardInfo(
            "PumpkinRings",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int HEAL = 8;

    public PumpkinRings()
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
}