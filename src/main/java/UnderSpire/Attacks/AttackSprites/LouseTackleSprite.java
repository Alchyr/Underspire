package UnderSpire.Attacks.AttackSprites;

import UnderSpire.Abstracts.Undertale.Attacks.AttackSprite;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import UnderSpire.Util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;

import static UnderSpire.UnderSpire.assetPath;

public class LouseTackleSprite extends AttackSprite {
    private static final float X_OFFSET = 100;
    private static final float SPEED = 300;

    private static final float width = 65;
    private static final float height = 39;

    private static final float xOffset = 0;
    private static final float yOffset = 0;

    private static final float BASE_SPAWNRATE = 1.5f;
    private static final float SPAWNRATE_SCALE = 0.1f;
    private static final float SPAWNRATE_CAP = 3.0f;

    private float spawnRate;

    private float maxDistance;

    private boolean flipped;

    public boolean isDone;

    public LouseTackleSprite(float startY, BattleArea area, int intensity, boolean flipped)
    {
        Texture LouseTexture = TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/Louse/Louse.png"));

        float startX;

        this.flipped = flipped;

        if (flipped)
        {
            startX = area.cX - area.width / 2 - X_OFFSET * Settings.scale - width * Settings.scale;
            maxDistance = area.cX + area.width / 2 + X_OFFSET * Settings.scale - width * Settings.scale;
        }
        else
        {
            startX = area.cX + area.width / 2 + X_OFFSET * Settings.scale;
            maxDistance = area.cX - area.width / 2 - X_OFFSET * Settings.scale;
        }

        spawnRate = Math.min(SPAWNRATE_CAP, BASE_SPAWNRATE + SPAWNRATE_SCALE * intensity);

        this.Initialize(LouseTexture, startX, startY, xOffset, yOffset, width, height, flipped, false);

        renderColor.a = 0.0f;

        isDone = false;
    }

    @Override
    public void updatePosition(float deltaTime, MinigameSoul p, BattleArea a) {
        if (flipped)
        {
            if (renderColor.a < 1.0f && x < maxDistance)
            {
                renderColor.a += spawnRate * deltaTime;
                if (renderColor.a > 1.0f)
                    renderColor.a = 1.0f;
            }
            else if (x < maxDistance)
            {
                x += SPEED * Settings.scale * deltaTime;
            }
            else
            {
                x += SPEED * Settings.scale * deltaTime;
                renderColor.a -= 1.5f * deltaTime;
                if (renderColor.a < 0.0f) {
                    renderColor.a = 0.0f;
                    isDone = true;
                }
            }
        }
        else
        {
            if (renderColor.a < 1.0f && x > maxDistance)
            {
                renderColor.a += spawnRate * deltaTime;
                if (renderColor.a > 1.0f)
                    renderColor.a = 1.0f;
            }
            else if (x > maxDistance)
            {
                x -= SPEED * Settings.scale * deltaTime;
            }
            else
            {
                x -= SPEED * Settings.scale * deltaTime;
                renderColor.a -= 1.5f * deltaTime;
                if (renderColor.a < 0.0f) {
                    renderColor.a = 0.0f;
                    isDone = true;
                }
            }
        }
    }
}