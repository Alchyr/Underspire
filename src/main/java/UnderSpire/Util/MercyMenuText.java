package UnderSpire.Util;

import UnderSpire.Patches.SpareableEnemies;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MercyMenuText extends VerticalMenuText {
    private static final String MERCY_TEXT = "MERCY";

    public MercyMenuText()
    {
        super(UndertaleText.TextStrings.get(MERCY_TEXT).FULL_TEXT);
    }

    @Override
    public void render(SpriteBatch sb, int asteriskX, int textX, int textY, float textWidth, float scale) {
        Fonts.UndertaleTextFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE * scale);
        sb.setColor(MinigameSoul.renderColor);
        sb.draw(MinigameSoul.soulTexture, asteriskX, textY - MinigameSoul.BASE_HEIGHT * Settings.scale - currentIndex * LINE_GAP * Settings.scale, 0, 0, MinigameSoul.BASE_WIDTH, MinigameSoul.BASE_HEIGHT, Settings.scale, Settings.scale, 0, 0, 0, MinigameSoul.BASE_INT_WIDTH, MinigameSoul.BASE_INT_HEIGHT, false, false);

        //Assume font color is white before this executes
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            if (!m.isDeadOrEscaped() && SpareableEnemies.Spareable.get(m))
            {
                Fonts.UndertaleTextFont.setColor(Color.YELLOW);
                break;
            }
        }

        Fonts.UndertaleTextFont.draw(sb, fullStrings.get(0), textX, textY, textWidth, Align.topLeft, true);

        Fonts.UndertaleTextFont.setColor(Color.WHITE);
        Fonts.UndertaleTextFont.draw(sb, fullStrings.get(1), textX, textY - LINE_GAP * Settings.scale, textWidth, Align.topLeft, true);

        Fonts.UndertaleTextFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE);
    }
}
