package UnderSpire.Interfaces;

import UnderSpire.Util.WeaponHits;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public interface WeaponCard {
    WeaponHits getWeaponHits();
    int getBaseDamage();

    AbstractGameAction.AttackEffect doAttackEffect(boolean crit);
}
