package UnderSpire.Effects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;


public class FallingSpriteEffect extends AbstractGameEffect {
    private Texture image;

    private float drawX;
    private float drawY;

    private float vX;
    private float vY;

    private static final float gY = -480.0f * Settings.scale;

    private float rotationSpeed;

    private int width;
    private int height;
    private float xOffset;
    private float yOffset;

    public FallingSpriteEffect(float cX, float cY, Color renderColor, Texture image, int imgWidth, int imgHeight, float initVX, float initVY, float rotationSpeed)
    {
        this.image = image;
        this.color = renderColor;
        this.rotation = 0.0f;
        this.scale = 1.0f;

        width = imgWidth;
        height = imgHeight;
        xOffset = width / 2.0f;
        yOffset = height / 2.0f;

        this.drawX = cX - xOffset;
        this.drawY = cY - yOffset;

        this.rotationSpeed = rotationSpeed;
        this.vX = initVX;
        this.vY = initVY;
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        this.drawX += vX * deltaTime;
        this.drawY += vY * deltaTime;
        this.vY += gY * deltaTime;
        this.rotation += rotationSpeed * deltaTime;

        if (this.drawY < -height * 2)
        {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(image, drawX, drawY, xOffset, yOffset, width, height, Settings.scale, Settings.scale, rotation, 0, 0, width, height, false, false);
    }

    @Override
    public void dispose() {
        image.dispose();
    }
}
