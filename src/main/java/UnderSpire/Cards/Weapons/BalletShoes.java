package UnderSpire.Cards.Weapons;

import UnderSpire.Abstracts.Undertale.BaseUndertaleCard;
import UnderSpire.Interfaces.WeaponCard;
import UnderSpire.Util.CardInfo;
import UnderSpire.Util.WeaponHits;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static UnderSpire.UnderSpire.makeID;

public class BalletShoes extends BaseUndertaleCard implements WeaponCard {
    private final static CardInfo cardInfo = new CardInfo(
            "BalletShoes",
            0,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    public BalletShoes()
    {
        super(cardInfo);
        this.purgeOnUse = true;
        this.baseDamage = 7;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public WeaponHits getWeaponHits() {
        return new WeaponHits(3, 300, 0.3f, 0.35f, WeaponHits.DirectionPreference.RIGHT);
    }
    public int getBaseDamage() {
        return this.baseDamage;
    }

    @Override    public AbstractGameAction.AttackEffect doAttackEffect(boolean critical)
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
