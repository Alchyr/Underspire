package UnderSpire.Attacks.AttackHandlers;

import UnderSpire.Abstracts.Undertale.Attacks.AttackHandler;
import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.Attacks.Patterns.JawWormChaseAttack;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;

import java.util.ArrayList;

public class JawWormAttackHandler extends AttackHandler {
    public JawWormAttackHandler()
    {
        super(JawWorm.ID);
    }

    @Override
    public ArrayList<AttackPattern> getAttackPattern(AbstractCreature source, ArrayList<AbstractGameAction> turnActions, int enemyCount) {
        ArrayList<AttackPattern> patterns = new ArrayList<>();

        int damage = 11;
        for (AbstractGameAction a : turnActions)
        {
            if (a.actionType == AbstractGameAction.ActionType.DAMAGE)
            {
                damage = a.amount;
            }
        }

        DamageInfo info = new DamageInfo(null, damage, DamageInfo.DamageType.NORMAL);

        if (source instanceof AbstractMonster)
        {
            switch (((AbstractMonster)source).intent)
            {
                case ATTACK_DEFEND:
                    patterns.add(new JawWormChaseAttack(info, enemyCount));
                    break;
                case ATTACK:
                    if (enemyCount > 1)
                    {
                        patterns.add(new JawWormChaseAttack(info, enemyCount));
                    }
                    else //This is the only enemy, use Big Bite attack
                    {
                        patterns.add(new JawWormChaseAttack(info, enemyCount));
                    }
                    break;
            }
        }

        return patterns;
    }
}
