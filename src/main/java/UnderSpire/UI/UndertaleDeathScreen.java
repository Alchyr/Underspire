package UnderSpire.UI;

import UnderSpire.Patches.UndertaleInput;
import UnderSpire.Util.TextureLoader;
import UnderSpire.Util.UndertaleText;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import static UnderSpire.UnderSpire.assetPath;

public class UndertaleDeathScreen extends DeathScreen {
    private UndertaleText deathMessage;
    private static Texture gameOverTitle;

    private static final int gameOverWidth = 416;
    private static final int gameOverHeight = 176;
    private static final float gameOverTitleX = MathUtils.floor(Settings.WIDTH / 2.0f - gameOverWidth / 2.0f);
    private static final float gameOverTitleY = MathUtils.floor(Settings.HEIGHT * 0.75f - gameOverHeight / 2.0f);
    private static final String GAME_OVER_TEXTURE = assetPath("img/UnderSpire/UI/GameOver.png");
    private static final float deathMessageWidth = 500 * Settings.scale;
    private static final float deathMessageXOffset = deathMessageWidth / 2.0f;
    private static final int deathMessageYPos = MathUtils.floor(Settings.HEIGHT * 0.25f);

    private boolean resetting;

    public UndertaleDeathScreen()
    {
        super(AbstractDungeon.getMonsters());
        AbstractDungeon.dynamicBanner.hide();
        this.returnButton.hide();
        deathMessage = new UndertaleText("DEATH");
        deathMessage.permanentText = true;
        if (gameOverTitle == null)
        {
            gameOverTitle = TextureLoader.getTexture(GAME_OVER_TEXTURE);
        }
        resetting = false;
    }


    public void update() {
        if (Settings.isDebug && InputHelper.justClickedRight) {
            UnlockTracker.resetUnlockProgress(AbstractDungeon.player.chosenClass);
        }

        this.deathMessage.update(Gdx.graphics.getDeltaTime());
        if (UndertaleInput.InputActions.undertaleConfirm.isJustPressed() || UndertaleInput.InputActions.undertaleBack.isJustPressed()) {

            if (Settings.isControllerMode) {
                Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
            }

            if (!deathMessage.done || resetting) {
            } else if (this.isVictory) {
                resetting = true;
                GameCursor.hidden = false;
                if (!AbstractDungeon.unlocks.isEmpty() && !Settings.isDemo) {
                    AbstractDungeon.unlocks.clear();
                    if (Settings.isDailyRun) {
                        CardCrawlGame.startOver();
                    } else {
                        CardCrawlGame.playCreditsBgm = false;
                        CardCrawlGame.startOverButShowCredits();
                    }
                    Settings.isTrial = false;
                    Settings.isDailyRun = false;
                    Settings.isEndless = false;
                    CardCrawlGame.trial = null;
                } else {
                    CardCrawlGame.startOver();
                }
            } else {
                resetting = true;
                GameCursor.hidden = false;
                CardCrawlGame.music.fadeOutTempBGM();

                if (!AbstractDungeon.unlocks.isEmpty() && !Settings.isDemo && !Settings.isDailyRun && !Settings.isTrial) {
                    AbstractDungeon.unlocks.clear();
                    Settings.isTrial = false;
                    Settings.isDailyRun = false;
                    Settings.isEndless = false;
                    CardCrawlGame.trial = null;
                    CardCrawlGame.playCreditsBgm = false;
                    CardCrawlGame.startOver();
                }
                else {
                    Settings.isTrial = false;
                    Settings.isDailyRun = false;
                    Settings.isEndless = false;
                    CardCrawlGame.trial = null;
                    CardCrawlGame.startOver();
                }
            }
        }

        AbstractDungeon.player.playDeathAnimation();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 771);
        sb.draw(gameOverTitle, gameOverTitleX, gameOverTitleY, gameOverWidth / 2.0f, gameOverHeight / 2.0f, gameOverWidth, gameOverHeight, Settings.scale, Settings.scale, 0, 0, 0, gameOverWidth, gameOverHeight, false, false);
        deathMessage.render(sb, -1000, MathUtils.floor(Settings.WIDTH / 2.0f - deathMessageXOffset), deathMessageYPos, deathMessageWidth, 1.0f);
    }
}
