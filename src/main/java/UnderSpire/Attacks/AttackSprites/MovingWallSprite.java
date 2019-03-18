package UnderSpire.Attacks.AttackSprites;

import UnderSpire.Abstracts.Undertale.Attacks.AttackSprite;
import UnderSpire.Attacks.Hitboxes.RectangleHitbox;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.core.Settings;

public class MovingWallSprite extends AttackSprite {
    public enum WallSide {
        BOTTOM,
        TOP,
        LEFT,
        RIGHT
    }

    private WallSide side; //The side the wall comes from
    private float distance;

    private static ShapeRenderer shapeRenderer;

    private RectangleHitbox hitbox;

    public MovingWallSprite(WallSide side, float startDistance)
    {
        if (shapeRenderer == null)
            shapeRenderer = new ShapeRenderer();

        this.side = side;
        this.distance = startDistance * Settings.scale;

        hitbox = new RectangleHitbox(0, 0, 0, 0);
    }

    public boolean checkHit(MinigameSoul p)
    {
        return hitbox.testHit(p, x, y);
    }


    public void render(float deltaTime, SpriteBatch sb, MinigameSoul player, BattleArea area) {
        updatePosition(deltaTime, player, area);
        draw(sb);
    }

    public void draw(SpriteBatch sb)
    {
        sb.end();

        if (shapeRenderer != null)
        {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(renderColor);

            shapeRenderer.rect(x, y, width, height);

            shapeRenderer.end();
        }

        sb.begin();
    }

    @Override
    public void updatePosition(float deltaTime, MinigameSoul p, BattleArea a) {
        switch (side)
        {
            case TOP:
                this.x = a.leftX;
                this.y = a.topY - distance;

                this.width = a.width;
                this.height = distance;
                break;
            case BOTTOM:
                this.x = a.leftX;
                this.y = a.bottomY;

                this.width = a.width;
                this.height = distance;
                break;
            case LEFT:
                this.x = a.leftX;
                this.y = a.bottomY;

                this.width = distance;
                this.height = a.height;
                break;
            case RIGHT:
                this.x = a.rightX - distance;
                this.y = a.bottomY;

                this.width = distance;
                this.height = a.height;
                break;
        }

        hitbox.width = width;
        hitbox.height = height;
    }

    public void dispose()
    {
        if (shapeRenderer != null)
        {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
    }
}