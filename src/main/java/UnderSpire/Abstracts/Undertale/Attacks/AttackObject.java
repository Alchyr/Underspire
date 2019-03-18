package UnderSpire.Abstracts.Undertale.Attacks;

import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AttackObject {
    public abstract boolean checkHit(MinigameSoul p);

    public abstract void render(float deltaTime, SpriteBatch sb, MinigameSoul p, BattleArea area);

    public abstract void dispose();
}
