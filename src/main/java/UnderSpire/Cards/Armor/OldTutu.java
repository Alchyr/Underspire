package UnderSpire.Cards.Armor;

import UnderSpire.Abstracts.Undertale.BaseUndertaleCard;
import UnderSpire.Interfaces.ArmorCard;
import UnderSpire.Util.CardInfo;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static UnderSpire.UnderSpire.makeID;

public class OldTutu extends BaseUndertaleCard implements ArmorCard {
    private final static CardInfo cardInfo = new CardInfo(
            "OldTutu",
            0,
            CardType.POWER,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    public OldTutu()
    {
        super(cardInfo);
        this.baseBlock = 10;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    @Override
    public int getDefense() {
        return this.baseBlock;
    }
}