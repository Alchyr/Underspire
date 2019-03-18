package UnderSpire.Effects;

import UnderSpire.Util.Fonts;
import UnderSpire.Util.UndertaleText;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class TextBoxEffect extends AbstractGameEffect {
    private static final float LINE_THICKNESS = 5.0f;
    private static final float DEFAULT_FONT_HEIGHT = 18.0f;
    private static float lineOffset() { return LINE_THICKNESS * Settings.scale / 2.0f; }

    private static final float xBorder = 20.0f;
    private static final float yBorder = 20.0f;

    private static final float defaultWidth = 575.0f;
    private static final float defaultHeight = 140.0f;

    private static final float defaultTopY = -340.0f;
    private static final float defaultBottomY = 200.0f;

    private UndertaleText text;

    private float textX;
    private float textY;
    private float textWidth;

    private float leftX;
    private float rightX;
    private float bottomY;
    private float topY;

    private float width;
    private float height;

    private ShapeRenderer shapeRenderer;

    private float scale;

    public TextBoxEffect(String textID)
    {
        this(new UndertaleText(textID));
    }
    public TextBoxEffect(UndertaleText text)
    {
        this(text, Settings.HEIGHT - defaultTopY * Settings.scale);
    }
    public TextBoxEffect(UndertaleText text, float cY)
    {
        this(text,Settings.WIDTH / 2.0f, cY, defaultWidth, defaultHeight);
    }
    public TextBoxEffect(UndertaleText text, float cX, float cY, float width, float height)
    {
        this.width = width * Settings.scale;
        this.height = height * Settings.scale;

        this.leftX = cX - this.width / 2.0f;
        this.rightX = cX + this.width / 2.0f;
        this.bottomY = cY - this.height / 2.0f;
        this.topY = cY + this.height / 2.0f;

        this.text = text;

        this.textX = cX - this.width / 2.0f + xBorder * Settings.scale;
        this.textY = cY + this.height / 2.0f - yBorder * Settings.scale;
        this.textWidth = width - xBorder * 2.0f * Settings.scale;
        this.scale = 1.0f;

        shapeRenderer = new ShapeRenderer();

        duration = 3.0f * text.fullTexts.size();
    }
    /*
    public TextBoxEffect(Texture charImage, float cX, float cY, float width, float height, UndertaleText text)
    {
        //uses a space on left side to render a character
        this.text = text;

    }*/

    public void setTextScale(float scale)
    {
        this.scale = scale;
    }

    @Override
    public void update() {
        if (!text.done && duration > 0.0f)
        {
            this.duration -= Gdx.graphics.getDeltaTime();
            text.update(Gdx.graphics.getDeltaTime());
        }
        else
        {
            this.isDone = true;
        }
    }

    @Override
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

        //render text
        Fonts.UndertaleTextFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE * scale);
        Fonts.UndertaleTextFont.draw(sb, text.currentText, textX, textY, textWidth, Align.topLeft, true);
        Fonts.UndertaleTextFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
