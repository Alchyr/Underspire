package UnderSpire.Attacks.Patterns;

/*
import UnderSpire.Attacks.Undertale.Attacks.AttackPattern;
import UnderSpire.Attacks.AttackObjects.JawWormBite;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class JawWormBiteAttack extends AttackPattern {
    private JawWormBite bite = null;

    public JawWormBiteAttack(DamageInfo info)
    {
        super(info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
    }

    private void spawn(BattleArea area)
    {
        bite = new JawWormBite(area.cX, area.cY, damageInfo.base);
    }

    @Override
    public void render(SpriteBatch sb, MinigameSoul p, BattleArea area) {
        if (bite == null)
        {
            spawn(area);
        }

        bite.render(sb, p, area);
    }

    @Override
    public boolean checkHit(MinigameSoul player) {
        if (bite != null)
            return bite.checkHit(player);

        return false;
    }

    @Override
    public void dispose() {
        if (bite != null)
            bite.dispose();
    }
}
*/