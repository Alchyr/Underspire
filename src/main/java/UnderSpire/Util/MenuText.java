package UnderSpire.Util;

import UnderSpire.Patches.UndertaleInput;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;

import java.util.ArrayList;

import static UnderSpire.UnderSpire.battleUI;
import static UnderSpire.Util.Sounds.MENU_MOVE;

public class MenuText extends UndertaleText {

    public int currentIndex;

    protected int pageCount;
    protected int currentPage;

    public ArrayList<String> fullStrings;

    public MenuText(String id)
    {
        this(TextStrings.get(id).FULL_TEXT);
    }
    public MenuText(String[] options)
    {
        super(options, -1);
        permanentText = true;

        pageCount = MathUtils.floor(this.fullTexts.size() / 6.0f); //0 - 5 = 0, etc
        currentPage = 0;
        currentIndex = 0;

        fullStrings = new ArrayList<>();

        for (char[] s : fullTexts)
        {
            fullStrings.add(new String(s));
        }
    }

    @Override
    public void update(float deltaTime) {
        boolean modified = false;
        if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed())
        {
            currentIndex--;
            modified = true;
        }

        if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed())
        {
            currentIndex++;
            modified = true;
        }

        if (InputActionSet.up.isJustPressed() || CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed())
        {
            currentIndex -= 2;
            modified = true;
        }

        if (InputActionSet.down.isJustPressed() || CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed())
        {
            modified = true;
            if (currentIndex % 2 != 0 && (currentPageSize() - (currentIndex % 6) == 2)) //on right side, and only one more element
            {
                currentIndex += 1;
            }
            else
            {
                currentIndex += 2;
            }
        }

        currentIndex = (currentIndex + fullStrings.size()) % fullStrings.size();
        currentPage = MathUtils.floor(currentIndex / 6.0f); //0 - 5 = 0, 6 - 11 = 1, etc.


        if (UndertaleInput.InputActions.undertaleBack.isJustPressed())
        {
            battleUI.back();
        }
        else if (modified)
        {
            CardCrawlGame.sound.play(MENU_MOVE.getKey());
        }
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
            Fonts.UndertaleMenuFont.draw(sb, fullStrings.get(i), textX + (i % 2 == 0 ? 0 : xOffset), textY - MathUtils.floor((i % 6) / 2.0f) * LINE_GAP * Settings.scale, textWidth, Align.topLeft, true);
        }
        Fonts.UndertaleMenuFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE);
    }

    public int currentPageSize()
    {
        if (currentPage < pageCount)
        {
            return 6;
        }
        else //On the last page, return last page size
        {
            return this.fullTexts.size() % 6;
        }
    }
}
