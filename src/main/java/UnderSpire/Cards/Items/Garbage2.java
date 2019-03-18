package UnderSpire.Cards.Items;

import UnderSpire.Abstracts.Undertale.ItemCard;
import UnderSpire.Util.CardInfo;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static UnderSpire.UnderSpire.makeID;

public class Garbage2 extends ItemCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Garbage2",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 1;

    public Garbage2()
    {
        super(cardInfo);
        FleetingField.fleeting.set(this, true);
        this.magicNumber = this.baseMagicNumber = DAMAGE;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        addDefaultTextActions();
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, this.magicNumber));
    }
}