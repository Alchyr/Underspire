package UnderSpire.Cards.Starter;

import UnderSpire.Abstracts.Undertale.ItemCard;
import UnderSpire.Interfaces.WeaponCard;
import UnderSpire.Util.CardInfo;
import UnderSpire.Util.Sounds;
import UnderSpire.Util.WeaponHits;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static UnderSpire.UnderSpire.makeID;

public class Stick extends ItemCard implements WeaponCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Stick",
            0,
            CardType.ATTACK,
            CardTarget.NONE,
            CardRarity.BASIC
    );

    public final static String ID = makeID(cardInfo.cardName);

    public Stick()
    {
        super(cardInfo);
        this.purgeOnUse = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction(Sounds.DOG_SONG.getKey()));
        addDefaultTextActions();
    }

    @Override
    public WeaponHits getWeaponHits() {
        return new WeaponHits(1, 250, 0, 0, WeaponHits.DirectionPreference.LEFT);
    }
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public AbstractGameAction.AttackEffect doAttackEffect(boolean critical)
    {
        if (critical)
        {
            return AbstractGameAction.AttackEffect.BLUNT_HEAVY;
        }
        else
        {
            return AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        }
    }
}