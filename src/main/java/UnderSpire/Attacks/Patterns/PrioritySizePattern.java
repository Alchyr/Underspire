package UnderSpire.Attacks.Patterns;

import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import javafx.util.Pair;

public class PrioritySizePattern extends AttackPattern {
    private int priority;
    private int width;
    private int height;

    public PrioritySizePattern(int priority, int width, int height)
    {
        super(null, AbstractGameAction.AttackEffect.NONE);

        this.priority = priority;
        this.width = width;
        this.height = height;
    }

    @Override
    public Pair<Integer, Pair<Integer, Integer>> preferredSize() {
        return new Pair<>(priority, new Pair<>(width, height));
    }

    @Override
    public boolean checkHit(MinigameSoul player) {
        return false;
    }
    @Override
    public void render(float deltaTime, SpriteBatch sb, MinigameSoul player, BattleArea area) {
    }
    @Override
    public void dispose() {
    }
}
