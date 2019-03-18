package UnderSpire.Util;

import UnderSpire.Patches.UndertaleInput;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;

import static UnderSpire.UnderSpire.battleUI;

public class VerticalMenuText extends MenuText {
    public VerticalMenuText(String id)
    {
        this(TextStrings.get(id).FULL_TEXT);
    }
    public VerticalMenuText(String[] options)
    {
        super(options);
    }



    @Override
    public void update(float deltaTime) {
        if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed() || InputActionSet.up.isJustPressed() || CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed())
        {
            currentIndex--;
        }

        if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed() || InputActionSet.down.isJustPressed() || CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed())
        {
            currentIndex++;
        }

        currentIndex = (currentIndex + fullStrings.size()) % fullStrings.size();

        if (UndertaleInput.InputActions.undertaleBack.isJustPressed())
        {
            battleUI.back();
        }
    }

    @Override
    public void render(SpriteBatch sb, int asteriskX, int textX, int textY, float textWidth, float scale) {
        Fonts.UndertaleMenuFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE * scale);
        sb.setColor(MinigameSoul.renderColor);
        sb.draw(MinigameSoul.soulTexture, asteriskX, textY - MinigameSoul.BASE_HEIGHT * Settings.scale - currentIndex * LINE_GAP * Settings.scale, 0, 0, MinigameSoul.BASE_WIDTH, MinigameSoul.BASE_HEIGHT, Settings.scale, Settings.scale, 0, 0, 0, MinigameSoul.BASE_INT_WIDTH, MinigameSoul.BASE_INT_HEIGHT, false, false);

        sb.setColor(Color.WHITE);
        for (int i = 0; i < fullStrings.size(); i++)
        {
            Fonts.UndertaleMenuFont.draw(sb, fullStrings.get(i), textX, textY - i * LINE_GAP * Settings.scale, textWidth, Align.topLeft, true);
        }
        Fonts.UndertaleMenuFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE);
    }
}
