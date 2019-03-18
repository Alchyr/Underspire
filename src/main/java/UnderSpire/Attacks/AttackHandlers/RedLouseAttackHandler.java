package UnderSpire.Attacks.AttackHandlers;

import UnderSpire.Abstracts.Undertale.Attacks.AttackHandler;
import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.Attacks.Patterns.LouseTackleAttack;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;

import java.util.ArrayList;

public class RedLouseAttackHandler extends AttackHandler {
    public RedLouseAttackHandler()
    {
        super(LouseNormal.ID);
    }

    @Override
    public ArrayList<AttackPattern> getAttackPattern(AbstractCreature source, ArrayList<AbstractGameAction> turnActions, int enemyCount) {
        ArrayList<AttackPattern> patterns = new ArrayList<>();

        int damage = 8;
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
                    patterns.add(new LouseTackleAttack(info));
                    break;
                case BUFF:
                    //Strength increase
                    break;
            }
        }

        return patterns;
    }
}