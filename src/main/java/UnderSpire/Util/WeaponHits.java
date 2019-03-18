package UnderSpire.Util;

import UnderSpire.Patches.UndertaleInput;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static UnderSpire.UnderSpire.assetPath;
import static UnderSpire.UnderSpire.battleUI;
import static UnderSpire.Util.Sounds.CRITICAL_ATTACK;
import static UnderSpire.Util.Sounds.NORMAL_ATTACK;

public class WeaponHits {
    private static final String barImage = assetPath("img/UnderSpire/UI/HitBar.png");
    private static final float INIT_DELAY = 0.0f;
    private static final float END_DELAY = 0.25f;

    private static final int BAR_WIDTH = 10;
    private static final int BAR_HEIGHT = 130;
    private static final float BAR_X_OFFSET = BAR_WIDTH / 2.0f;
    private static final float BAR_Y_OFFSET = BAR_HEIGHT / 2.0f;

    private static final float VALID_DISTANCE = 280.0f;

    public float cX;
    public float yPos;
    private float minPos;
    private float maxPos;

    public float minGap;
    public float maxGap;

    public int numHits;

    public float barSpeed;

    private int currentHitIndex;
    private int activeHitIndex;

    private float timeToNextHit;

    public AbstractMonster target;

    private ArrayList<Float> hitPositions;
    private ArrayList<Float> hitDirections; //1 for right, -1 for left
    private ArrayList<Float> hitScales; //when press key, increase scale slightly and stop moving, increase scale
    private ArrayList<Color> hitColors;
    private ArrayList<Float> hitGrades;

    private static Texture barTexture;

    private boolean finishing;

    public boolean critical;

    public enum DirectionPreference
    {
        LEFT,
        RIGHT,
        EITHER
    }

    public WeaponHits(int numHits, float speed, float minGap, float maxGap, DirectionPreference direction)
    {
        this.numHits = numHits;
        this.barSpeed = speed * Settings.scale;

        hitPositions = new ArrayList<>();
        hitScales = new ArrayList<>();
        hitColors = new ArrayList<>();
        hitGrades = new ArrayList<>();
        hitDirections = new ArrayList<>();

        cX = battleUI.getCenterX();
        yPos = battleUI.getCenterY();
        minPos = cX - VALID_DISTANCE * Settings.scale;
        maxPos = cX + VALID_DISTANCE * Settings.scale;

        critical = false;

        for (int i = 0; i < numHits; i++)
        {
            hitColors.add(new Color(1.0f, 1.0f, 1.0f, 0.0f));
            hitScales.add(1.0f);
            switch (direction)
            {
                case RIGHT:
                    hitDirections.add(1.0f);
                    hitPositions.add(battleUI.getHitLeftPosition());
                    break;
                case LEFT:
                    hitDirections.add(-1.0f);
                    hitPositions.add(battleUI.getHitRightPosition());
                    break;
                case EITHER:
                    if (MathUtils.randomBoolean())
                    {
                        hitDirections.add(1.0f);
                        hitPositions.add(battleUI.getHitLeftPosition());
                    }
                    else
                    {
                        hitDirections.add(-1.0f);
                        hitPositions.add(battleUI.getHitRightPosition());
                    }
                    break;
            }
        }
        this.minGap = minGap;
        this.maxGap = maxGap;
        activeHitIndex = 0;
        currentHitIndex = -1;
        timeToNextHit = INIT_DELAY;
        finishing = false;

        if (barTexture == null)
        {
            barTexture = TextureLoader.getTexture(barImage);
        }
    }

    public boolean update(float deltaTime)
    {

        timeToNextHit -= deltaTime;
        if (finishing && timeToNextHit <= 0)
        {
            return true;
        }
        else if (timeToNextHit <= 0 && currentHitIndex < hitPositions.size())
        {
            currentHitIndex++;
            timeToNextHit = MathUtils.random(minGap, maxGap);
        }
        else if (activeHitIndex >= hitPositions.size() && !finishing)
        {
            finishing = true;
            timeToNextHit = END_DELAY;
        }

        if (currentHitIndex >= 0)
        {
            for (int i = 0; i < activeHitIndex; i++)
            {
                hitColors.get(i).a -= deltaTime * 3.0f;
                if (hitColors.get(i).a < 0.0f)
                    hitColors.get(i).a = 0.0f;
            }

            for (int i = activeHitIndex; i < currentHitIndex; i++) {
                if (hitColors.get(i).a < 1.0f) {
                    hitColors.get(i).a += deltaTime * 2.0f;
                    if (hitColors.get(i).a > 1.0f) {
                        hitColors.get(i).a = 1.0f;
                    }
                }
                hitPositions.set(i, hitPositions.get(i) + hitDirections.get(i) * barSpeed * deltaTime);
            }

                //Check for key presses
            if (UndertaleInput.InputActions.undertaleConfirm.isJustPressed() && activeHitIndex < hitPositions.size())
            {
                //perform hit check
                float dist = java.lang.Math.abs(hitPositions.get(activeHitIndex) - cX);

                if (dist < 3.0f * Settings.scale)
                {
                    hitGrades.add(2.0f);
                    hitColors.get(activeHitIndex).r = 0.0f;
                    hitColors.get(activeHitIndex).g = 0.75f;
                    hitColors.get(activeHitIndex).a = 0.8f;
                    CardCrawlGame.sound.play(CRITICAL_ATTACK.getKey());
                }
                else
                {
                    hitGrades.add((VALID_DISTANCE * Settings.scale - dist) / VALID_DISTANCE * Settings.scale);
                    hitColors.get(activeHitIndex).b = 0.0f;
                    hitColors.get(activeHitIndex).g = 0.0f;
                    hitColors.get(activeHitIndex).a = 0.8f;
                    CardCrawlGame.sound.play(NORMAL_ATTACK.getKey());
                }

                //Play hit sound, get from current weapon card

                //set current hit color based on the hit

                hitScales.set(activeHitIndex, 1.3f);

                activeHitIndex++;
            }

            while (activeHitIndex < hitDirections.size() && (hitDirections.get(activeHitIndex) < 0 ? hitPositions.get(activeHitIndex) < minPos : hitPositions.get(activeHitIndex) > maxPos))
            {
                hitGrades.add(0.0f);
                activeHitIndex++;
            }
        }
        return false;
    }

    public void render(SpriteBatch sb)
    {
        if (activeHitIndex > 0)
        {
            for (int i = 0; i < activeHitIndex; i++)
            {
                sb.setColor(hitColors.get(i));
            }
        }
        for (int i = 0; i < hitPositions.size(); i++)
        {
            sb.setColor(hitColors.get(i));
            sb.draw(barTexture, hitPositions.get(i) - BAR_X_OFFSET, yPos - BAR_Y_OFFSET, BAR_X_OFFSET, BAR_Y_OFFSET, BAR_WIDTH, BAR_HEIGHT, Settings.scale * hitScales.get(i), Settings.scale * hitScales.get(i), 0, 0, 0, BAR_WIDTH, BAR_HEIGHT, false, false);
        }
    }


    public float getDamageModifier()
    {
        if (hitGrades.size() > 0)
        {
            float average = 0;
            boolean perfect = true;

            for (float grade : hitGrades)
            {
                if (grade <= 1.0f)
                {
                    perfect = false;
                    average += grade;
                }
                else //perfect
                {
                    average += 1.25f; //full perfect is 1.5x, partial is only a bonus
                }
            }

            if (perfect)
            {
                critical = true;
                return 1.5f;
            }

            return average / hitGrades.size();
        }
        return 0;
    }
}
