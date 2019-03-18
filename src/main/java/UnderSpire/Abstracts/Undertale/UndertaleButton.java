package UnderSpire.Abstracts.Undertale;

import UnderSpire.Patches.UndertaleInput;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

import static UnderSpire.UnderSpire.assetPath;
import static UnderSpire.Util.Sounds.MENU_SELECT;

public abstract class UndertaleButton {
    public static final String FIGHT_TEXTURE = assetPath("img/UnderSpire/Buttons/Fight.png");
    public static final String FIGHT_SELECTED_TEXTURE = assetPath("img/UnderSpire/Buttons/Fight_s.png");
    public static final String ACT_TEXTURE = assetPath("img/UnderSpire/Buttons/Act.png");
    public static final String ACT_SELECTED_TEXTURE = assetPath("img/UnderSpire/Buttons/Act_s.png");
    public static final String ITEM_TEXTURE = assetPath("img/UnderSpire/Buttons/Item.png");
    public static final String ITEM_SELECTED_TEXTURE = assetPath("img/UnderSpire/Buttons/Item_s.png");
    public static final String MERCY_TEXTURE = assetPath("img/UnderSpire/Buttons/Mercy.png");
    public static final String MERCY_SELECTED_TEXTURE = assetPath("img/UnderSpire/Buttons/Mercy_s.png");



    public static int BASE_WIDTH = 112;
    public static int BASE_HEIGHT = 44;

    public static float SOUL_OFFSET_X = 10;
    public static float SOUL_OFFSET_Y = 13;

    private Texture image;
    private Texture selectedImage;

    public boolean selected;

    private float x;
    private float y;

    public float angle;

    public UndertaleButton(float x, float y, Texture image, Texture selectedImage)
    {
        this.image = image;
        this.selectedImage = selectedImage;
        this.selected = false;

        this.x = x;
        this.y = y;

        this.angle = 0;
    }

    public void update(float deltaTime, boolean selected, boolean enabled)
    {
        this.selected = selected;
        if (selected && enabled && UndertaleInput.InputActions.undertaleConfirm.isJustPressed())
        {
            CardCrawlGame.sound.play(MENU_SELECT.getKey());
            performAction();
        }
    }

    public abstract void performAction();

    public void render(SpriteBatch sb, boolean displaySoul)
    {
        sb.setColor(Color.WHITE);
        int drawX = MathUtils.floor(x);
        int drawY = MathUtils.floor(y);
        sb.draw(selected ? selectedImage : image, drawX, drawY, 0, 0, BASE_WIDTH, BASE_HEIGHT, Settings.scale, Settings.scale, angle, 0, 0, BASE_WIDTH, BASE_HEIGHT, false, false);

        if (displaySoul && selected)
        {
            sb.setColor(MinigameSoul.renderColor);
            sb.draw(MinigameSoul.soulTexture, MathUtils.floor(drawX + SOUL_OFFSET_X * Settings.scale), MathUtils.floor(drawY + SOUL_OFFSET_Y * Settings.scale), -MathUtils.floor(SOUL_OFFSET_X * Settings.scale), -MathUtils.floor(SOUL_OFFSET_Y * Settings.scale), MinigameSoul.BASE_WIDTH, MinigameSoul.BASE_HEIGHT, Settings.scale, Settings.scale, angle, 0, 0, MinigameSoul.BASE_INT_WIDTH, MinigameSoul.BASE_INT_HEIGHT, false, false);
        }
    }
}
