package UnderSpire.Attacks.Patterns;

import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.Attacks.AttackObjects.JawWormChase;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import javafx.util.Pair;

public class JawWormChaseAttack extends AttackPattern {
    private JawWormChase chase = null;

    private int targetWidth;
    private int targetHeight;

    public JawWormChaseAttack(DamageInfo info, int enemyCount)
    {
        super(info, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        targetWidth = 80 + enemyCount * 20;
        targetHeight = 80 + enemyCount * 20;
    }

    private void spawn(MinigameSoul p, BattleArea area)
    {
        float distance = area.width;

        float angle = MathUtils.random() * MathUtils.PI * 2.0f;

        float xOff = MathUtils.cos(angle) * distance;
        float yOff = MathUtils.sin(angle) * distance;

        chase = new JawWormChase(p.cX + xOff, p.cY + yOff, damageInfo.base);
    }

    @Override
    public Pair<Integer, Pair<Integer, Integer>> preferredSize() {
        return new Pair<>(NO_PRIORITY_AREA, new Pair<>(targetWidth, targetHeight));
    }

    @Override
    public void render(float deltaTime, SpriteBatch sb, MinigameSoul p, BattleArea area) {
        if (chase == null)
        {
            spawn(p, area);
        }

        chase.render(deltaTime, sb, p, area);
    }

    @Override
    public boolean checkHit(MinigameSoul player) {
        if (chase != null)
            return chase.checkHit(player);

        return false;
    }

    @Override
    public void dispose() {
        if (chase != null)
            chase.dispose();
    }
}