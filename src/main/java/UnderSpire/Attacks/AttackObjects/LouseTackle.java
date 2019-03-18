package UnderSpire.Attacks.AttackObjects;

import UnderSpire.Abstracts.Undertale.Attacks.AttackObject;
import UnderSpire.Attacks.AttackSprites.LouseTackleSprite;
import UnderSpire.Attacks.Hitboxes.RectangleHitbox;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;

public class LouseTackle extends AttackObject {
    private static final float Y_OFFSET = -14.0f;

    private LouseTackleSprite spr;
    private static RectangleHitbox hitbox = new RectangleHitbox(55.0f * Settings.scale, 23.0f * Settings.scale, 8 * Settings.scale, 4 * Settings.scale);

    public boolean isDone;

    public LouseTackle(MinigameSoul player, BattleArea area, int intensity)
    {
        float yOffset = Y_OFFSET + MathUtils.random(-2.0f, 2.0f);
        if (player.cY < area.cY - area.height / 4) //in bottom 1/4, don't push them down
        {
            yOffset -= MathUtils.random(3.0f, 8.0f);
        }
        else if (player.cY > area.cY + area.height / 4) //in top 1/4, don't push them up
        {
            yOffset += MathUtils.random(3.0f, 8.0f);
        }

        yOffset *= Settings.scale;

        spr = new LouseTackleSprite(player.cY + yOffset, area, intensity, MathUtils.randomBoolean());

        this.isDone = false;
    }

    @Override
    public boolean checkHit(MinigameSoul p) {
        return hitbox.testHit(p, spr.x, spr.y);
    }

    @Override
    public void render(float deltaTime, SpriteBatch sb, MinigameSoul player, BattleArea area) {
        spr.render(deltaTime, sb, player, area);
        this.isDone = spr.isDone;
    }

    @Override
    public void dispose() {
        spr.dispose();
    }
}
