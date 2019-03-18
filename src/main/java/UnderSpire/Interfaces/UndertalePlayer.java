package UnderSpire.Interfaces;

import UnderSpire.Util.WeaponHits;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface UndertalePlayer {
    AbstractGameAction.AttackEffect doAttackEffect(boolean critical);
    DamageInfo getAttackInfo();
    void equipWeapon(WeaponCard toEquip);
    AbstractCard getEquippedWeaponAsCard();
    WeaponCard getEquippedWeapon();

    void equipArmor(ArmorCard toEquip);
    AbstractCard getEquippedArmorAsCard();
    ArmorCard getEquippedArmor();

    WeaponHits getWeaponHits();
    void performAttack(WeaponHits hits);
}
