package UnderSpire.Attacks.AttackObjects;

import UnderSpire.Abstracts.Undertale.Attacks.AttackObject;
import UnderSpire.Attacks.AttackSprites.JawWormChaseSprite;
import UnderSpire.Attacks.Hitboxes.CircleHitbox;
import UnderSpire.UI.MinigameSoul;
import UnderSpire.UI.BattleArea;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public class JawWormChase extends AttackObject {
    private static final float ACCEL = 200.0f;
    private static final float ACCEL_SCALE = 22.0f;

    private JawWormChaseSprite spr;
    private static CircleHitbox hitbox = new CircleHitbox(18.0f * Settings.scale, 0, 0);

    public JawWormChase(float startX, float startY, int intensity)
    {
        spr = new JawWormChaseSprite(startX, startY, ACCEL + ACCEL_SCALE * intensity);
    }

    @Override
    public boolean checkHit(MinigameSoul p) {
        return hitbox.testHit(p, spr.cX, spr.cY);
    }

    @Override
    public void render(float deltaTime, SpriteBatch sb, MinigameSoul player, BattleArea area) {
        spr.render(deltaTime, sb, player, area);
    }

    @Override
    public void dispose() {
        spr.dispose();
    }
}
