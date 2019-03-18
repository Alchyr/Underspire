package UnderSpire.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.actions.unique.DeckToHandAction;
import com.megacrit.cardcrawl.cards.blue.Seek;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.util.ArrayList;

import static UnderSpire.UnderSpire.assetPath;

public class Fonts {
    private static FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private static FreeTypeFontGenerator generator;
    private static FreeTypeFontGenerator.FreeTypeBitmapFontData data = new FreeTypeFontGenerator.FreeTypeBitmapFontData();
    private static FileHandle fontFile = null;


    private static final String DIALOGUE_FONT = "fonts/DIALOGUE.fnt";
    private static final String MENU_FONT = "fonts/MENU.fnt";

    //NOTE - For future self if returning to ttf
    //Correct base size for font is 32 instead of 24 like it should be. Why the heck? i don't know.

    public static BitmapFont UndertaleTextFont;
    public static BitmapFont UndertaleMenuFont;

    public static final float TEXT_FONT_DEFAULT_SCALE = 1.0f;


    public Fonts() {}
    public static void Initialize()
    {
        fontFile = Gdx.files.internal(assetPath(DIALOGUE_FONT));
        UndertaleTextFont = prepSimpleFont();
        fontFile = Gdx.files.internal(assetPath(MENU_FONT));
        UndertaleMenuFont = prepSimpleFont();
    }

    private static BitmapFont prepSimpleFont()
    {
        BitmapFont textFont = new BitmapFont(fontFile, false);
        textFont.getData().markupEnabled = true;
        if (LocalizedStrings.break_chars != null) {
            textFont.getData().breakChars = LocalizedStrings.break_chars.toCharArray();
        }
        return textFont;
    }

    private static BitmapFont prepFont(float size, boolean isLinearFiltering) {
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.characters = "";
        p.incremental = true;
        p.gamma = param.gamma;
        p.size = java.lang.Math.round(size * Settings.scale);
        p.spaceX = param.spaceX;
        p.spaceY = param.spaceY;
        p.borderColor = param.borderColor;
        p.borderStraight = param.borderStraight;
        p.borderWidth = param.borderWidth;
        p.borderGamma = param.borderGamma;
        p.shadowColor = param.shadowColor;
        p.shadowOffsetX = param.shadowOffsetX;
        p.shadowOffsetY = param.shadowOffsetY;
        if (isLinearFiltering) {
            p.minFilter = Texture.TextureFilter.Linear;
            p.magFilter = Texture.TextureFilter.Linear;
        } else {
            p.minFilter = Texture.TextureFilter.Nearest;
            p.magFilter = Texture.TextureFilter.Nearest;
        }

        BitmapFont font = g.generateFont(p);
        font.setUseIntegerPositions(!isLinearFiltering);
        font.getData().markupEnabled = true;
        if (LocalizedStrings.break_chars != null) {
            font.getData().breakChars = LocalizedStrings.break_chars.toCharArray();
        }

        return font;
    }
}
