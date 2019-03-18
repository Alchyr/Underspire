package UnderSpire.Util;

import UnderSpire.Patches.UndertaleInput;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static UnderSpire.UnderSpire.logger;
import static UnderSpire.UnderSpire.textSoundMap;
import static UnderSpire.Util.Sounds.NORMAL_TEXT;
import static UnderSpire.Util.Sounds.TEXT_A;

public class UndertaleText {
    protected static final float LINE_GAP = 36 * Settings.scale; //measure it with screenshot. This is to adjust soul and text.

    public static HashMap<String, UndertaleTextBase> TextStrings;

    public ArrayList<char[]> fullTexts;
    public String currentText;
    public int currentTextIndex;
    public int currentLetter;
    public float rate;
    public float toNextLetter;

    public boolean useMenuFont;

    public boolean permanentText;
    public boolean done;

    public String[] soundSet;

    public static void Initialize(String file)
    {
        try
        {
            Gson gson = new Gson();
            String json = Gdx.files.internal(file).readString(String.valueOf(StandardCharsets.UTF_8));

            Type type = new TypeToken<Map<String, UndertaleTextBase>>(){}.getType();
            Map<String, UndertaleTextBase> map = gson.fromJson(json, type);

            TextStrings = new HashMap<>();
            for (Map.Entry<String, UndertaleTextBase> entry : map.entrySet())
            {
                TextStrings.put(entry.getKey(), entry.getValue());
            }

            for (UndertaleTextBase base : TextStrings.values())
            {
                for (int i = 0; i < base.FULL_TEXT.length; i++)
                {
                    base.FULL_TEXT[i] = base.FULL_TEXT[i].replace("!NAME!",System.getProperty("user.name"));
                }
            }
        }
        catch (Exception e)
        {
            logger.info("Loading Undertale text strings failed.");
        }
    }

    public UndertaleText(String id)
    {
        this(TextStrings.get(id));
    }
    public UndertaleText(UndertaleTextBase base)
    {
        this(base.FULL_TEXT, base.RATE, textSoundMap.get(base.SOUND_KEY), base.MENU_FONT);
    }
    public UndertaleText(UndertaleText copy)
    {
        this.fullTexts = new ArrayList<>();

        fullTexts.addAll(copy.fullTexts);

        this.currentTextIndex = 0;
        this.currentText = "";
        this.rate = copy.rate;
        this.toNextLetter = 0;
        currentLetter = 0;
        done = false;
        permanentText = false;

        this.soundSet = copy.soundSet;
        this.useMenuFont = copy.useMenuFont;
    }
    public UndertaleText(String[] fullText, float rate)
    {
        this(fullText, rate, textSoundMap.get("NORMAL_TEXT"), false);
    }
    public UndertaleText(String[] fullText, float rate, String[] soundSet, boolean useMenuFont)
    {
        this.fullTexts = new ArrayList<>();
        for (String s : fullText)
        {
            fullTexts.add(s.toCharArray());
        }

        this.soundSet = soundSet;
        this.useMenuFont = useMenuFont;

        this.currentTextIndex = 0;
        this.currentText = "";
        this.rate = rate;
        this.toNextLetter = 0;
        currentLetter = 0;
        done = false;
        permanentText = false;
    }

    public void update(float deltaTime)
    {
        if (currentTextIndex < fullTexts.size())
        {
            if (currentLetter < fullTexts.get(currentTextIndex).length)
            {
                if (UndertaleInput.InputActions.undertaleBack.isJustPressed() || rate == -1.0f)
                {
                    skip();
                }
                else
                {
                    toNextLetter -= deltaTime;
                    if (toNextLetter <= 0)
                    {
                        toNextLetter = rate;
                        currentText += fullTexts.get(currentTextIndex)[currentLetter];
                        if (fullTexts.get(currentTextIndex)[currentLetter] != ' ')
                        {
                            playTextSound();
                        }
                        currentLetter++;
                    }
                }
            }
            else if (UndertaleInput.InputActions.undertaleConfirm.isJustPressed() ||
                    UndertaleInput.InputActions.undertaleBack.isJustPressed())
            {
                currentTextIndex++;
                if (currentTextIndex < fullTexts.size() || !permanentText)
                {
                    currentText = "";
                    toNextLetter = 0;
                    currentLetter = 0;
                }
            }
        }
        else if (!done)
        {
            done = true;
        }
    }

    public void render(SpriteBatch sb, int asteriskX, int textX, int textY, float textWidth, float scale)
    {
        BitmapFont font = useMenuFont ? Fonts.UndertaleMenuFont : Fonts.UndertaleTextFont;
        font.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE * scale);
        font.draw(sb, "*", asteriskX, textY, textWidth, Align.topLeft, true);
        if (currentText.contains("\n"))
        {
            int nextAsteriskY = textY;

            nextAsteriskY -= LINE_GAP;

            String preNewline = currentText.substring(0, currentText.indexOf('\n'));
            if (FontHelper.getWidth(font, preNewline, scale) > textWidth)
            {
                nextAsteriskY -= LINE_GAP;
            }
            font.draw(sb, "*", asteriskX, nextAsteriskY, textWidth, Align.topLeft, true);

            if (currentText.indexOf('\n') < currentText.length() - 1)
            {
                String postNewLine = currentText.substring(currentText.indexOf('\n') + 1);
                if (postNewLine.contains("\n")) //not checking new line because text should only have 3 lines at most
                {
                    nextAsteriskY -= LINE_GAP;
                    font.draw(sb, "*", asteriskX, nextAsteriskY, textWidth, Align.topLeft, true);
                }
            }
        }
        font.draw(sb, currentText, textX, textY, textWidth, Align.topLeft, true);
        font.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE);
    }
    public void skip()
    {
        playTextSound();
        currentText = new String(fullTexts.get(currentTextIndex));
        currentLetter = fullTexts.get(currentTextIndex).length;
    }

    private void playTextSound()
    {
        CardCrawlGame.sound.play(soundSet[MathUtils.random(0, soundSet.length - 1)]);
    }

    public void reset()
    {
        currentText = "";
        currentLetter = 0;
        currentTextIndex = 0;
        done = false;
    }
}
