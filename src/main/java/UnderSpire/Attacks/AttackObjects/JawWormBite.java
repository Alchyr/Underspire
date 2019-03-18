package UnderSpire.Attacks.AttackObjects;

/*
import UnderSpire.Attacks.Undertale.Attacks.AttackObject;
import UnderSpire.Attacks.Undertale.Attacks.AttackHitbox;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import UnderSpire.Util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;

import java.util.ArrayList;

import static UnderSpire.UnderSpire.assetPath;

public class JawWormBite extends AttackObject {
    //image info:
    //Jaw side: Origin: 58, 191
    //draw one jaw rotate from -30 -> -5, one jaw 30 -> 5 with horizontal flip
    //Jaw Bottom: Use this as main spawn location: offset 116, 47
    //Jaw sides are drawn at same y as jaw bottom, x offset 29

    //Attach teeth to random anchor points on insides of jaw
    //Use origin same as Jaw Side origin so that it rotates smoothly

    private static final float JawBottomX = 116.0F;
    private static final float JawBottomY = 47.0F;

    private static final float JawMouthX = 58.0F;
    private static final float JawMouthY = 47.0F;
    private static final float JawMouthOffset = 116.0F;

    private static final float ToothLeftXOffset = 114.0F;
    private static final float ToothRightXOffset = 2.0F;
    private static final float ToothYOffset = 187.0F;

    private Color renderColor;

    private static final float SPEED = 0.0f;

    private static final float ACCEL = 3.0f;
    private static final float ACCEL_SCALE = 0.25f;

    private float angleSpeed;
    private float angleAccel;

    private float cX;
    private float cY;

    private float angle;

    private static final float START_ANGLE = 20.0f;
    private static final float MIN_ANGLE = 4.0f;
    private static final float YOffset = 200;

    private ArrayList<Vector2> LeftTeeth = new ArrayList<>();
    private ArrayList<Vector2> RightTeeth = new ArrayList<>();

    public Texture JawWormMouth;
    public Texture JawWormBottom;
    public Texture JawWormTooth;

    public JawWormBite(float cX, float cY, int intensity)
    {
        super(cX - JawBottomX * Settings.scale, cY - YOffset * Settings.scale, new AttackHitbox[] {});
        renderColor = new Color(1, 1, 1, 0);

        angleSpeed = SPEED;
        angleAccel = ACCEL + ACCEL_SCALE * intensity;

        this.cX = cX;
        this.cY = cY - YOffset;

        angle = START_ANGLE;

        LeftTeeth.add(new Vector2(ToothLeftXOffset, ToothYOffset));
        RightTeeth.add(new Vector2(ToothRightXOffset, ToothYOffset));




        JawWormBottom = TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/JawWorm/JawBottom.png"));
        JawWormMouth = TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/JawWorm/JawSide.png"));
        JawWormTooth = TextureLoader.getTexture(assetPath("img/UnderSpire/Attacks/JawWorm/Tooth.png"));
    }

    @Override
    public boolean checkHit(MinigameSoul p) {
        boolean hit = false;
        for (AttackHitbox h : hitboxes)
        {
            if (h.testHit(p, cX, cY))
                hit = true;
        }
        return hit;
    }

    @Override
    public void render(SpriteBatch sb, MinigameSoul player, BattleArea area) {
        float time = Gdx.graphics.getDeltaTime();

        if (renderColor.a < 1.0f)
        {
            renderColor.a += 0.2f;
            if (renderColor.a > 1.0f)
                renderColor.a = 1.0f;
        }
        else
        {
            updateMovement(time);
        }

        sb.setColor(renderColor);

        float mouthLeftX = x;
        float mouthRightX = x + JawMouthOffset * Settings.scale;

        for (Vector2 pos : LeftTeeth)
        {
            sb.draw(JawWormTooth, mouthLeftX + pos.x, y + pos.y, JawMouthX - pos.x, JawMouthY - pos.y, JawWormTooth.getWidth(), JawWormTooth.getHeight(), Settings.scale, Settings.scale, angle, 0, 0, 58, 58, false, false);
        }
        for (Vector2 pos : RightTeeth)
        {
            sb.draw(JawWormTooth, mouthRightX + pos.x, y + pos.y, JawMouthX - pos.x, JawMouthY - pos.y, JawWormTooth.getWidth(), JawWormTooth.getHeight(), Settings.scale, Settings.scale, -angle, 0, 0, 58, 58, false, false);
        }


        sb.draw(JawWormMouth, mouthLeftX, y, JawMouthX, JawMouthY, JawWormMouth.getWidth(), JawWormMouth.getHeight(), Settings.scale, Settings.scale, angle, 0, 0, 116, 238, false, false);
        sb.draw(JawWormMouth, mouthRightX, y, JawMouthX, JawMouthY, JawWormMouth.getWidth(), JawWormMouth.getHeight(), Settings.scale, Settings.scale, -angle, 0, 0, 116, 238, true, false);

        sb.draw(JawWormBottom, x, y, JawBottomX, JawBottomY, JawWormBottom.getWidth(), JawWormBottom.getHeight(), Settings.scale, Settings.scale, 0, 0, 0, 232, 94, false, false);
    }


    private void updateMovement(float deltaTime)
    {
        angle -= angleSpeed * deltaTime;
        if (angle < MIN_ANGLE)
            angle = MIN_ANGLE;

        angleSpeed += angleAccel * deltaTime;
    }

    @Override
    public void dispose() {
        JawWormTooth.dispose();
        JawWormBottom.dispose();
        JawWormMouth.dispose();
    }
}
*/