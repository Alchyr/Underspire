package UnderSpire.Effects;

import UnderSpire.Util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static UnderSpire.UnderSpire.assetPath;

public class BlackScreenEffect extends AbstractGameEffect {
    private static Texture black;

    public BlackScreenEffect(float duration)
    {
        this.duration = duration;
        this.color = Color.BLACK;

        if (black == null)
            black = TextureLoader.getTexture(assetPath("img/UnderSpire/Black.png"));
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.color.a = 0.0F;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(black, 0, 0, 0, 0, Settings.WIDTH, Settings.HEIGHT, 1, 1, 0, 0, 0, 1, 1, false, false);
    }

    @Override
    public void dispose() {
    }
}
