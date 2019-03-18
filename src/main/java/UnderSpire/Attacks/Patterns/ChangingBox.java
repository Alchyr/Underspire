package UnderSpire.Attacks.Patterns;

import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ChangingBox extends AttackPattern {
    private float startWidth;
    private float startHeight;

    private float targetWidth;
    private float targetHeight;

    private float startX;
    private float startY;

    private float targetX;
    private float targetY;

    private boolean byScale;
    private float targetScale;

    private float targetTime;
    private float elapsedTime;

    private boolean initialized;
    private boolean finished;

    public ChangingBox(float targetWidth, float targetHeight, float targetX, float targetY, float time)
    {
        super(null, AbstractGameAction.AttackEffect.NONE);

        initialized = false;
        finished = false;

        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;

        this.targetX = targetX;
        this.targetY = targetY;

        this.targetTime = time;

        byScale = false;
    }
    public ChangingBox(float targetScale, float time)
    {
        super(null, AbstractGameAction.AttackEffect.NONE);

        initialized = false;
        finished = false;

        this.targetScale = targetScale;
        this.byScale = true;

        this.targetTime = time;
    }

    public void InitializeStart(BattleArea area)
    {
        this.startWidth = area.width;
        this.startHeight = area.height;
        this.startX = area.cX;
        this.startY = area.cY;

        if (byScale)
        {
            this.targetX = startX;
            this.targetY = startY;

            this.targetWidth = startWidth * targetScale;
            this.targetHeight = startWidth * targetScale;
        }

        this.elapsedTime = 0.0f;
        this.initialized = true;
        this.finished = false;
    }

    @Override
    public boolean checkHit(MinigameSoul player) {
        return false;
    }

    @Override
    public void render(float deltaTime, SpriteBatch sb, MinigameSoul player, BattleArea area) {
        if (!initialized)
        {
            this.InitializeStart(area);
        }

        if (!this.finished)
        {
            this.elapsedTime += deltaTime;
            if (this.elapsedTime < this.targetTime)
            {
                float progress = elapsedTime / targetTime;
                area.move(MathUtils.lerp(startX, targetX, progress), MathUtils.lerp(startY, targetY, progress));
                area.resize(MathUtils.lerp(startWidth, targetWidth, progress), MathUtils.lerp(startHeight, targetHeight, progress));
            }
            else //ensure it gets to the target size
            {
                area.move(targetX, targetY);
                area.resize(targetWidth, targetHeight);
                this.finished = true;
            }
        }
    }

    @Override
    public void dispose() {

    }
}
