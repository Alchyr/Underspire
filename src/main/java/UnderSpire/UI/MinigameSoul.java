package UnderSpire.UI;

import UnderSpire.Util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import static UnderSpire.UnderSpire.assetPath;

public class MinigameSoul {
    public static final Color defaultColor = new Color(1.0F, 0.0F, 0.0F, 1.0F);
    public static final Texture soulTexture = TextureLoader.getTexture(assetPath("img/UnderSpire/Soul.png"));
    public static final Texture crackedTexture = TextureLoader.getTexture(assetPath("img/UnderSpire/Cracked.png"));

    public float cX;
    public float cY;

    public float width;
    public float height;

    public static final float BASE_WIDTH = 16.0F;
    public static final float BASE_HEIGHT = 16.0F;

    public static final int BASE_INT_WIDTH = 16;
    public static final int BASE_INT_HEIGHT = 16;

    private float xChange = 0;
    private boolean horizonalBoth = false;
    private float yChange = 0;
    private boolean verticalBoth = false;

    public static float SPEED = 150.0f;
    public static float INVINCIBLE = 1.5f;

    public float invincibleTime = 0.0f;

    public static Color renderColor = defaultColor.cpy();

    public MinigameSoul()
    {
        renderColor = defaultColor.cpy();

        cX = Settings.WIDTH / 2.0f;
        cY = Settings.HEIGHT / 2.0f;

        width = BASE_WIDTH * Settings.scale;
        height = BASE_HEIGHT * Settings.scale;
    }

    public void setPos(float cX, float cY)
    {
        this.cX = cX;
        this.cY = cY;
    }

    public void renderUI(SpriteBatch sb, BattleArea area)
    {
        //Override for special color souls with their own controls that might need their own UI
    }

    public static void draw(SpriteBatch sb, float cX, float cY, float scale)
    {
        sb.setColor(renderColor);
        sb.draw(soulTexture, cX - BASE_WIDTH * 0.5f, cY - BASE_HEIGHT * 0.5f, BASE_WIDTH * 0.5f, BASE_HEIGHT * 0.5f, BASE_WIDTH, BASE_HEIGHT, Settings.scale * scale, Settings.scale * scale, 0, 0, 0, BASE_INT_WIDTH, BASE_INT_HEIGHT, false, false);
    }
    public static void drawCracked(SpriteBatch sb, float cX, float cY, float scale)
    {
        sb.setColor(renderColor);
        sb.draw(crackedTexture, cX - BASE_WIDTH * 0.5f, cY - BASE_HEIGHT * 0.5f, BASE_WIDTH * 0.5f, BASE_HEIGHT * 0.5f, BASE_WIDTH, BASE_HEIGHT, Settings.scale * scale, Settings.scale * scale, 0, 0, 0, BASE_INT_WIDTH, BASE_INT_HEIGHT, false, false);
    }

    public void render(SpriteBatch sb, BattleArea area)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if (!horizonalBoth)
            {
                horizonalBoth = true;
                xChange *= -1;
            }

            if (xChange < 0)
                xChange = -SPEED;

            if (xChange >= 0)
                xChange = SPEED;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            horizonalBoth = false;
            xChange = -SPEED;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            horizonalBoth = false;
            xChange = SPEED;
        }
        else
        {
            horizonalBoth = false;
            xChange = 0.0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            if (!verticalBoth)
            {
                verticalBoth = true;
                yChange *= -1;
            }

            if (yChange < 0)
                yChange = -SPEED;

            if (yChange >= 0)
                yChange = SPEED;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            verticalBoth = false;
            yChange = SPEED;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            verticalBoth = false;
            yChange = -SPEED;
        }
        else
        {
            verticalBoth = false;
            yChange = 0.0f;
        }

        if (invincibleTime > 0.0f)
            invincibleTime -= Gdx.graphics.getDeltaTime();
        xChange *= Gdx.graphics.getDeltaTime();
        yChange *= Gdx.graphics.getDeltaTime();


        this.cX += xChange;
        this.cY += yChange;

        area.update(this);

        if (invincibleTime <= 0.0f || (invincibleTime % 0.16f < 0.08f))
        {
            draw(sb, cX, cY, 1.0f);
        }
    }
    public void render(SpriteBatch sb, boolean cracked)
    {
        if (cracked)
        {
            drawCracked(sb, cX, cY, 1.0f);
        }
        else
        {
            draw(sb, cX, cY, 1.0f);
        }
    }
}
