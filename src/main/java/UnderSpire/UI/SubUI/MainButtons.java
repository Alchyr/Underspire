package UnderSpire.UI.SubUI;

import UnderSpire.Abstracts.Undertale.UndertaleButton;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;

import static UnderSpire.Util.Sounds.MENU_MOVE;

//Fight, Act, Items, Mercy
public class MainButtons {
    public static final float LEFT_X_OFFSET = 4.0f; //measured from base game screenshot
    public static final float BUTTON_X_GAP = 157.0f; //measured from base game screenshot
    public static final float BUTTON_Y_GAP = 86.0f; //measured from base game screenshot

    private UndertaleButton[] buttons;

    private int currentIndex;

    public boolean active;
    public boolean enabled; //if false, still visible, but no selection

    public MainButtons(TextBox mainDisplay)
    {
        float leftX = mainDisplay.leftX - LEFT_X_OFFSET;
        float bottomY = mainDisplay.bottomY - BUTTON_Y_GAP;

        buttons = new UndertaleButton[4];

        buttons[0] = new FightButton(leftX, bottomY);
        leftX += BUTTON_X_GAP * Settings.scale;
        buttons[1] = new ActButton(leftX, bottomY);
        leftX += BUTTON_X_GAP * Settings.scale;
        buttons[2] = new ItemButton(leftX, bottomY);
        leftX += BUTTON_X_GAP * Settings.scale;
        buttons[3] = new MercyButton(leftX, bottomY);

        active = true; //whether or not player is selected from main menu or a sub-menu
        enabled = true; //Whether or not there is active text (such as when using an item, or checking an enemy)
        //also determines if player soul is rendered on buttons

        currentIndex = 0;
    }

    public void update(float deltaTime)
    {
        if (active && enabled)
        {
            boolean changed = false;

            if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
                currentIndex--;
                changed = true;
            }

            if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                currentIndex++;
                changed = true;
            }

            currentIndex = (currentIndex + 4) % 4;

            if (changed)
            {
                CardCrawlGame.sound.play(MENU_MOVE.getKey());
            }
        }

        int index = 0;
        for (UndertaleButton b : buttons)
        {
            b.update(deltaTime, enabled && (currentIndex == index), active && enabled);
            index++;
        }
    }

    public void render(SpriteBatch sb)
    {
        for (UndertaleButton b : buttons)
        {
            b.render(sb, active);
        }
    }
}
