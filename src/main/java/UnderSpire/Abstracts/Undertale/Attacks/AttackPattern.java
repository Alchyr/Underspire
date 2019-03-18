package UnderSpire.Abstracts.Undertale.Attacks;

import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import javafx.util.Pair;

public abstract class AttackPattern {
    public static final int NO_PRIORITY_AREA = 0;
    public static final int LOW_PRIORITY_AREA = 1;
    public static final int MED_PRIORITY_AREA = 2;
    public static final int HIGH_PRIORITY_AREA = 4;

    public AttackPattern(DamageInfo info, AbstractGameAction.AttackEffect effect)
    {
        this.attackEffect = effect;
        this.damageInfo = info;
    }

    public abstract void render(float deltaTime, SpriteBatch sb, MinigameSoul player, BattleArea area);
    public abstract boolean checkHit(MinigameSoul player);
    public abstract void dispose();

    public void addCustomHitEffects()
    {}

    public Pair<Integer, Pair<Integer, Integer>> preferredSize()
    {
        return null;
    }

    public AbstractGameAction.AttackEffect attackEffect;
    public DamageInfo damageInfo;
}
