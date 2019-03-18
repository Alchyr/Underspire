package UnderSpire.Util;

import UnderSpire.Patches.SpareableEnemies;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class EnemyMenuText extends MenuText {
    private ArrayList<AbstractMonster> enemies;

    public EnemyMenuText(String[] options, ArrayList<AbstractMonster> enemies)
    {
        super(options);

        this.enemies = enemies;
    }


    @Override
    public void render(SpriteBatch sb, int asteriskX, int textX, int textY, float textWidth, float scale) {
        int xOffset = MathUtils.floor(textWidth / 2.0f);

        Fonts.UndertaleMenuFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE * scale);
        sb.setColor(MinigameSoul.renderColor);
        sb.draw(MinigameSoul.soulTexture, asteriskX + (currentIndex % 2 == 0 ? 0 : xOffset), textY - MinigameSoul.BASE_HEIGHT * Settings.scale - MathUtils.floor((currentIndex % 6) / 2.0f) * LINE_GAP * Settings.scale, 0, 0, MinigameSoul.BASE_WIDTH, MinigameSoul.BASE_HEIGHT, Settings.scale, Settings.scale, 0, 0, 0, MinigameSoul.BASE_INT_WIDTH, MinigameSoul.BASE_INT_HEIGHT, false, false);
        sb.setColor(Color.WHITE);

        for (int i = currentPage * 6; i < (currentPage * 6) + currentPageSize(); i++)
        {
            if (SpareableEnemies.Spareable.get(enemies.get(i)))
            {
                Fonts.UndertaleMenuFont.setColor(Color.YELLOW);
            }
            else
            {
                Fonts.UndertaleMenuFont.setColor(Color.WHITE);
            }
            Fonts.UndertaleMenuFont.draw(sb, fullStrings.get(i), textX + (i % 2 == 0 ? 0 : xOffset), textY - MathUtils.floor((i % 6) / 2.0f) * LINE_GAP * Settings.scale, textWidth, Align.topLeft, true);
        }
        Fonts.UndertaleMenuFont.setColor(Color.WHITE);
        Fonts.UndertaleMenuFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE);
    }
}
