package UnderSpire.UI.SubUI;

import UnderSpire.Actions.General.EndTurnNowAction;
import UnderSpire.Interfaces.UndertalePlayer;
import UnderSpire.Patches.UndertaleInput;
import UnderSpire.UI.BattleUI;
import UnderSpire.Util.MenuText;
import UnderSpire.Util.TextureLoader;
import UnderSpire.Util.UndertaleText;
import UnderSpire.Util.WeaponHits;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import javax.xml.soap.Text;

import static UnderSpire.UnderSpire.assetPath;
import static UnderSpire.UnderSpire.battleUI;

public class TextBox {
    private static final float LINE_THICKNESS = 5.0f;
    private static final String ATTACK_AREA_TEXTURE = assetPath("img/UnderSpire/UI/Fight.png");

    private static final int ATTACK_AREA_WIDTH = 547;
    private static final int ATTACK_AREA_HEIGHT = 116;
    private static final float ATTACK_AREA_X_OFFSET = ATTACK_AREA_WIDTH / 2.0f;
    private static final float ATTACK_AREA_Y_OFFSET = ATTACK_AREA_HEIGHT / 2.0f;

    public static float lineOffset() { return LINE_THICKNESS * Settings.scale / 2.0f; }


    private static final float ASTERISK_OFFSET = 26.0f;
    private static final float xBorder = 16.0f;
    private static final float yBorder = 24.0f;

    private static final float defaultWidth = 575.0f;
    private static final float defaultHeight = 140.0f;

    private static final float defaultTopY = -200.0f;
    private static final float defaultBottomY = 200.0f;

    private UndertaleText text;
    public TextState state;

    public enum TextState
    {
        NONE,
        MENU, //Buttons still highlighted
        ACTIVE, //Buttons not highlighted
        ATTACKING //Attack thing.
    }

    public float cX;
    public float cY;

    private int asteriskX;
    private int textX;
    private int textY;
    private float textWidth;

    public float leftX;
    public float rightX;
    public float bottomY;
    public float topY;

    private float width;
    private float height;

    private ShapeRenderer shapeRenderer;
    private static Texture attackTexture = null;

    private WeaponHits hits;

    private float scale;

    public TextBox()
    {
        this(defaultBottomY);
    }
    public TextBox(float cY)
    {
        this(Settings.WIDTH / 2.0f, cY, defaultWidth, defaultHeight);
    }
    public TextBox(float cX, float cY, float width, float height)
    {
        this.cX = cX;
        this.cY = cY;

        this.width = width * Settings.scale;
        this.height = height * Settings.scale;

        this.leftX = cX - this.width / 2.0f;
        this.rightX = cX + this.width / 2.0f;
        this.bottomY = cY - this.height / 2.0f;
        this.topY = cY + this.height / 2.0f;

        this.asteriskX = Math.round(cX - this.width / 2.0f + xBorder * Settings.scale);
        this.textX = Math.round(asteriskX + ASTERISK_OFFSET * Settings.scale);
        this.textY = Math.round(cY + this.height / 2.0f - yBorder * Settings.scale);
        this.textWidth = width - xBorder * 2.0f * Settings.scale - ASTERISK_OFFSET * 2.0f * Settings.scale;
        this.scale = 1.0f;

        shapeRenderer = new ShapeRenderer();
        if (attackTexture == null)
        {
            attackTexture = TextureLoader.getTexture(ATTACK_AREA_TEXTURE);
        }

        state = TextState.NONE;
    }

    public void setTextScale(float scale)
    {
        this.scale = scale;
    }

    public void setText(UndertaleText text)
    {
        state = TextState.NONE;
        this.text = text;
    }
    public void setActiveText(UndertaleText text)
    {
        state = TextState.ACTIVE;
        this.text = text;
    }
    public void setMenuText(MenuText text)
    {
        state = TextState.MENU;
        this.text = text;
    }
    public void startAttack(WeaponHits hits)
    {
        state = TextState.ATTACKING;
        this.text = null;
        this.hits = hits;
    }
    public void clearText()
    {
        state = TextState.NONE;
        this.text = null;
    }
    public boolean isDone()
    {
        return (text == null) || (text.done);
    }

    public void update(float deltaTime) {
        if (state == TextState.MENU && text instanceof MenuText)
        {
            text.update(deltaTime);
            if (UndertaleInput.InputActions.undertaleConfirm.isJustPressed())
            {
                battleUI.menuPressed(((MenuText) text).currentIndex);
            }
        }
        else if (state == TextState.ATTACKING)
        {
            if (hits != null)
            {
                if (hits.update(deltaTime))
                {
                    //hits are done, perform attack
                    if (AbstractDungeon.player instanceof UndertalePlayer)
                    {
                        ((UndertalePlayer) AbstractDungeon.player).performAttack(hits);
                    }

                    AbstractDungeon.actionManager.addToBottom(new EndTurnNowAction());

                    hits = null;
                    state = TextState.NONE;
                }
            }
            else
            {
                state = TextState.NONE;
            }
        }
        else if (text != null)
        {
            if (!text.done)
            {
                text.update(deltaTime);
            }
            else if (!text.permanentText)
            {
                text = null;
                state = TextState.NONE;
            }
            else
            {
                state = TextState.NONE;
            }
        }
        else if (state != TextState.NONE)
        {
            state = TextState.NONE;
        }
    }

    public void render(SpriteBatch sb) {
        //render box
        sb.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);

        shapeRenderer.rect(leftX, bottomY, width, height);

        shapeRenderer.setColor(1, 1, 1, 1);

        shapeRenderer.rectLine(leftX - lineOffset(), bottomY, rightX + lineOffset(), bottomY, LINE_THICKNESS * Settings.scale);
        shapeRenderer.rectLine(leftX - lineOffset(), topY, rightX + lineOffset(), topY, LINE_THICKNESS * Settings.scale);
        shapeRenderer.rectLine(leftX, bottomY - lineOffset(), leftX, topY + lineOffset(), LINE_THICKNESS * Settings.scale);
        shapeRenderer.rectLine(rightX, bottomY - lineOffset(), rightX, topY + lineOffset(), LINE_THICKNESS * Settings.scale);

        shapeRenderer.end();

        sb.begin();

        if (state != TextState.ATTACKING && text != null)
        {
            text.render(sb, asteriskX, textX, textY, textWidth, scale);
        }
        else if (state == TextState.ATTACKING && hits != null)
        {
            sb.setColor(Color.WHITE);
            sb.draw(attackTexture, cX - ATTACK_AREA_X_OFFSET, cY - ATTACK_AREA_Y_OFFSET, ATTACK_AREA_X_OFFSET, ATTACK_AREA_Y_OFFSET, ATTACK_AREA_WIDTH, ATTACK_AREA_HEIGHT, Settings.scale, Settings.scale, 0, 0, 0, ATTACK_AREA_WIDTH, ATTACK_AREA_HEIGHT, false, false);
            hits.render(sb);
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
        attackTexture.dispose();
        attackTexture = null;
    }
}