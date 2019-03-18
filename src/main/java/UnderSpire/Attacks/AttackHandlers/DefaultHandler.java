package UnderSpire.Attacks.AttackHandlers;

import UnderSpire.Abstracts.Undertale.Attacks.AttackHandler;
import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.Attacks.Patterns.JawWormChaseAttack;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class DefaultHandler extends AttackHandler {
    public DefaultHandler()
    {
        super("");
    }

    @Override
    public ArrayList<AttackPattern> getAttackPattern(AbstractCreature source, ArrayList<AbstractGameAction> turnActions, int enemyCount) {
        ArrayList<AttackPattern> patterns = new ArrayList<>();

        int damage = 10;
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
                case ATTACK:
                case ATTACK_DEFEND:
                case ATTACK_BUFF:
                case ATTACK_DEBUFF:
                    patterns.add(new JawWormChaseAttack(info, enemyCount));
                    break;
            }
        }

        return patterns;
    }
}