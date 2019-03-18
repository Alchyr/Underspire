package UnderSpire.Cards.Items;

import UnderSpire.Abstracts.Undertale.ItemCard;
import UnderSpire.Util.CardInfo;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static UnderSpire.UnderSpire.makeID;

public class HushPuppy extends ItemCard {
    private final static CardInfo cardInfo = new CardInfo(
            "HushPuppy",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int HEAL = 65;

    public HushPuppy()
    {
        super(cardInfo);
        FleetingField.fleeting.set(this, true);
        this.magicNumber = this.baseMagicNumber = HEAL;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        addDefaultTextActions();
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
        //TODO: Add some kind of special effect
    }
}