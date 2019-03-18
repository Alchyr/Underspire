package UnderSpire.UI;

import UnderSpire.Util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.core.Settings;

import static UnderSpire.UnderSpire.assetPath;

public class BattleArea {
    //Calculated based on width/height and player width/height
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    public float leftX;
    public float rightX;
    public float bottomY;
    public float topY;

    public float width;
    public float height;

    public float cX;
    public float cY;

    private ShapeRenderer shapeRenderer;
    private static Texture black;

    private static final float LINE_THICKNESS = 5;
    private static float lineOffset() { return LINE_THICKNESS * Settings.scale / 2.0f; }

    public BattleArea(float width, float height)
    {
        cX = Settings.WIDTH / 2.0f;
        cY = Settings.HEIGHT / 2.0f;

        leftX = cX - (width * Settings.scale * 0.5f);
        rightX = cX + (width * Settings.scale * 0.5f);
        bottomY = cY - (height * Settings.scale * 0.5f);
        topY = cY + (height * Settings.scale * 0.5f);


        minX = leftX + (MinigameSoul.BASE_WIDTH * Settings.scale * 0.5f) + lineOffset();
        maxX = rightX - (MinigameSoul.BASE_WIDTH * Settings.scale * 0.5f) - lineOffset();
        minY = bottomY + (MinigameSoul.BASE_HEIGHT * Settings.scale * 0.5f) + lineOffset();
        maxY = topY - (MinigameSoul.BASE_HEIGHT * Settings.scale * 0.5f) - lineOffset();

        this.width = rightX - leftX;
        this.height = topY - bottomY;

        shapeRenderer = new ShapeRenderer();

        if (black == null)
        {
            black = TextureLoader.getTexture(assetPath("img/UnderSpire/Black.png"));
        }
    }

    public void update(MinigameSoul player)
    {
        checkPos(player);
    }

    public void render(SpriteBatch sb)
    {
        sb.draw(black, cX, cY, 0.5f, 0.5f, 1.0f, 1.0f, width, height, 0.0f, 0, 0, 1, 1, false, false);

        sb.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);

        shapeRenderer.rectLine(leftX - lineOffset(), bottomY, rightX + lineOffset(), bottomY, LINE_THICKNESS * Settings.scale);
        shapeRenderer.rectLine(leftX - lineOffset(), topY, rightX + lineOffset(), topY, LINE_THICKNESS * Settings.scale);
        shapeRenderer.rectLine(leftX, bottomY - lineOffset(), leftX, topY + lineOffset(), LINE_THICKNESS * Settings.scale);
        shapeRenderer.rectLine(rightX, bottomY - lineOffset(), rightX, topY + lineOffset(), LINE_THICKNESS * Settings.scale);

        shapeRenderer.end();

        sb.begin();
    }

    public void move(float newcX, float newcY)
    {
        this.cX = newcX;
        this.cY = newcY;

        leftX = cX - (width * 0.5f);
        rightX = cX + (width * 0.5f);
        bottomY = cY - (height * 0.5f);
        topY = cY + (height * 0.5f);

        minX = leftX + (MinigameSoul.BASE_WIDTH * Settings.scale * 0.5f) + lineOffset();
        maxX = rightX - (MinigameSoul.BASE_WIDTH * Settings.scale * 0.5f) - lineOffset();
        minY = bottomY + (MinigameSoul.BASE_HEIGHT * Settings.scale * 0.5f) + lineOffset();
        maxY = topY - (MinigameSoul.BASE_HEIGHT * Settings.scale * 0.5f) - lineOffset();
    }
    public void resize(float newWidth, float newHeight)
    {
        this.width = newWidth;
        this.height = newHeight;

        leftX = cX - (width * 0.5f);
        rightX = cX + (width * 0.5f);
        bottomY = cY - (height * 0.5f);
        topY = cY + (height * 0.5f);

        minX = leftX + (MinigameSoul.BASE_WIDTH * Settings.scale * 0.5f) + lineOffset();
        maxX = rightX - (MinigameSoul.BASE_WIDTH * Settings.scale * 0.5f) - lineOffset();
        minY = bottomY + (MinigameSoul.BASE_HEIGHT * Settings.scale * 0.5f) + lineOffset();
        maxY = topY - (MinigameSoul.BASE_HEIGHT * Settings.scale * 0.5f) - lineOffset();
    }


    private void checkPos(MinigameSoul player)
    {
        if (player.cX < minX)
            player.cX = minX;

        if (player.cX > maxX)
            player.cX = maxX;

        if (player.cY < minY)
            player.cY = minY;

        if (player.cY > maxY)
            player.cY = maxY;
    }

    public void dispose()
    {
        shapeRenderer.dispose();
    }
}
