package UnderSpire.Attacks.AttackSprites;

import UnderSpire.Abstracts.Undertale.Attacks.AttackSprite;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import UnderSpire.Util.Math;
import UnderSpire.Util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;

import static UnderSpire.UnderSpire.assetPath;

public class JawWormChaseSprite extends AttackSprite {
    private static final float frameTime = 0.25f;
    private static final float FRICTION = 0.975f;

    private static final float width = 28;
    private static final float height = 28;

    private static final float xOffset = 14;
    private static final float yOffset = 14;

    public float cX;
    public float cY;

    private float accel;

    private float xSpeed;
    private float ySpeed;

    public JawWormChaseSprite(float cX, float cY, float accel)
    {
        Texture[] JawWormChaseTextures = new Texture[] {
                TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/JawWorm/Spawn1.png")),
                TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/JawWorm/Spawn2.png")),
                TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/JawWorm/Spawn3.png")),
                TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/JawWorm/Chase1.png")),
                TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/JawWorm/Chase2.png"))
        };

        this.accel = accel * Settings.scale;

        this.cX = cX;
        this.cY = cY;

        float startX = cX - xOffset;
        float startY = cY - yOffset;

        this.Initialize(JawWormChaseTextures, frameTime, startX, startY, xOffset, yOffset, width, height, false, false);
    }

    @Override
    public void updatePosition(float deltaTime, MinigameSoul p, BattleArea a) {
        if (currentFrame < 3)
        {
            angle = Math.angle(cX, p.cX, cY, p.cY);
        }
        else
        {
            updateMovement(p, deltaTime);
        }
    }

    private void updateMovement(MinigameSoul p, float deltaTime)
    {
        angle = Math.radAngle(cX, p.cX, cY, p.cY);

        float xChange = (MathUtils.cos(angle)) * accel * deltaTime;
        float yChange = (MathUtils.sin(angle)) * accel * deltaTime;

        //Friction helps follow more closely
        xSpeed *= FRICTION;
        ySpeed *= FRICTION;

        xSpeed += xChange;
        ySpeed += yChange;

        angle = Math.angle(0, xSpeed, 0, ySpeed);

        cX += xSpeed * deltaTime;
        cY += ySpeed * deltaTime;

        x = cX - xOffset;
        y = cY - yOffset;
    }

    @Override
    public int getNextFrame() {
        switch (currentFrame)
        {
            case 0:
            case 1:
            case 2:
            case 3:
                return ++currentFrame;
            default:
                return 3;
        }
    }
}
