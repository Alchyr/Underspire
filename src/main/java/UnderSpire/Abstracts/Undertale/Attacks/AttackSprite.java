package UnderSpire.Abstracts.Undertale.Attacks;

import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public abstract class AttackSprite {
    public static final float NO_ANIM = -1.0f;

    //Bottom left corner of draw position
    public float x;
    public float y;

    private Texture[] frames;
    private float[] frameLengths;
    public int currentFrame;
    public float currentFrameTime;

    public float angle; //for drawing
    public float xOffset; //Offset of center from bottom left corner, positive is up/right
    public float yOffset;

    public float width;
    public float height;

    public boolean flipX;
    public boolean flipY;

    public Color renderColor = Color.WHITE.cpy();

    public AttackSprite() {
        //nothing. If this is used, initialize with Initialize
    }

    public AttackSprite(Texture texture, float startX, float startY, float xOffset, float yOffset, float width, float height, boolean flipX, boolean flipY)
    {
        this(new Texture[] { texture }, new float[] { -1.0f }, startX, startY, xOffset, yOffset, width, height, flipX, flipY);
    }

    public AttackSprite(Texture[] textures, float[] frameLengths, float startX, float startY, float xOffset, float yOffset, float width, float height, boolean flipX, boolean flipY)
    {
        this.frames = textures;
        this.frameLengths = frameLengths.clone();
        this.currentFrame = 0;
        this.currentFrameTime = frameLengths[0];
        x = startX;
        y = startY;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        this.width = width;
        this.height = height;

        this.flipX = flipX;
        this.flipY = flipY;

        this.angle = 0;
    }

    public void Initialize(Texture texture, float startX, float startY, float xOffset, float yOffset, float width, float height, boolean flipX, boolean flipY)
    {
        Texture[] textures = new Texture[] { texture };
        float[] frameTimes = new float[] { NO_ANIM };

        this.Initialize(textures, frameTimes, startX, startY, xOffset, yOffset, width, height, flipX, flipY);
    }

    public void Initialize(Texture[] textures, float frameTime, float startX, float startY, float xOffset, float yOffset, float width, float height, boolean flipX, boolean flipY)
    {
        float[] frameTimes = new float[textures.length];
        for (int i = 0; i < frameTimes.length; i++)
        {
            frameTimes[i] = frameTime;
        }

        this.Initialize(textures, frameTimes, startX, startY, xOffset, yOffset, width, height, flipX, flipY);
    }

    public void Initialize(Texture[] textures, float[] frameLengths, float startX, float startY, float xOffset, float yOffset, float width, float height, boolean flipX, boolean flipY)
    {
        this.frames = textures;
        this.frameLengths = frameLengths.clone();
        this.currentFrame = 0;
        this.currentFrameTime = frameLengths[0];
        x = startX;
        y = startY;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        this.width = width;
        this.height = height;

        this.flipX = flipX;
        this.flipY = flipY;

        this.angle = 0;
    }

    public void updateFrame(float deltaTime) {
        currentFrameTime -= deltaTime;
        if (currentFrameTime <= 0.0f) {
            currentFrame = getNextFrame();
            currentFrameTime = frameLengths[currentFrame];
        }
    }

    public int getNextFrame()
    {
        return 0;
    }

    public void render(float deltaTime, SpriteBatch sb, MinigameSoul player, BattleArea area) {
        updatePosition(deltaTime, player, area);
        if (frameLengths[currentFrame] != NO_ANIM)
            updateFrame(deltaTime);
        draw(sb);
    }

    public abstract void updatePosition(float deltaTime, MinigameSoul p, BattleArea a);

    public void draw(SpriteBatch sb)
    {
        if (currentFrame >= 0 && currentFrame < frames.length)
        {
            sb.setColor(renderColor);
            sb.draw(frames[currentFrame], x, y, xOffset, yOffset, width, height, Settings.scale, Settings.scale, angle, 0, 0, frames[currentFrame].getWidth(), frames[currentFrame].getHeight(), flipX, flipY);
        }
    }

    public void dispose()
    {
        for (Texture t : frames)
        {
            t.dispose();
        }
    }
}
