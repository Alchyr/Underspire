package UnderSpire.Abstracts.Undertale.Attacks;

import UnderSpire.Attacks.AttackHandlers.GreenLouseAttackHandler;
import UnderSpire.Attacks.AttackHandlers.JawWormAttackHandler;
import UnderSpire.Attacks.AttackHandlers.RedLouseAttackHandler;
import UnderSpire.UnderSpire;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

public abstract class AttackHandler {
    public static void InitializeHandlers()
    {
        UnderSpire.addAttackHandler(new JawWormAttackHandler());
        UnderSpire.addAttackHandler(new GreenLouseAttackHandler());
        UnderSpire.addAttackHandler(new RedLouseAttackHandler());
    }


    public String id;

    public AttackHandler(String id)
    {
        this.id = id;
    }

    public abstract ArrayList<AttackPattern> getAttackPattern(AbstractCreature source, ArrayList<AbstractGameAction> turnActions, int enemyCount);
}