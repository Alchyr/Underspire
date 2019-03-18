package UnderSpire.Effects;

import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.Relics.YourSoul;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import UnderSpire.Util.Sounds;
import UnderSpire.Util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.util.ArrayList;

import static UnderSpire.UnderSpire.assetPath;
import static UnderSpire.Util.Sounds.*;

public class EnemyAttackEffect extends AbstractGameEffect {
    private ArrayList<AttackPattern> attackPatterns;
    private BattleArea area;
    private MinigameSoul player;

    private static Texture black;

    private static final String HeartPieceTexture1 = assetPath("img/UnderSpire/Piece1.png");
    private static final int Piece1Width = 8;
    private static final int Piece1Height = 8;
    private static final String HeartPieceTexture2 = assetPath("img/UnderSpire/Piece2.png");
    private static final int Piece2Width = 8;
    private static final int Piece2Height = 11;
    private static final String HeartPieceTexture3 = assetPath("img/UnderSpire/Piece3.png");
    private static final int Piece3Width = 7;
    private static final int Piece3Height = 9;
    private static final String HeartPieceTexture4 = assetPath("img/UnderSpire/Piece4.png");
    private static final int Piece4Width = 7;
    private static final int Piece4Height = 11;

    public static boolean dying;
    public DyingState deathState = DyingState.ALIVE;

    private static final float DYING_TIME = 1.0f;
    private static final float SHATTER_TIME = 3.0f;

    public enum DyingState
    {
        ALIVE,
        DYING,
        SHATTERED,
        DONE
    }

    private static Color renderColor = Color.WHITE.cpy();

    public EnemyAttackEffect(BattleArea area, MinigameSoul player, ArrayList<AttackPattern> attackPatterns, float duration)
    {
        this.duration = duration;
        this.attackPatterns = attackPatterns;
        this.area = area;
        this.player = player;

        if (black == null)
            black = TextureLoader.getTexture(assetPath("img/UnderSpire/Black.png"));

        dying = false;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F && !dying) {
            this.duration = 666f;
            this.isDone = true;
        }
        else
        {
            switch (deathState)
            {
                case DYING:
                    if (duration < 0)
                    {
                        deathState = DyingState.SHATTERED;
                        duration = SHATTER_TIME;

                        CardCrawlGame.sound.play(SOUL_SHATTER.getKey());

                        AbstractDungeon.topLevelEffectsQueue.add(new FallingSpriteEffect(player.cX, player.cY,
                                MinigameSoul.renderColor, TextureLoader.getTexture(HeartPieceTexture1), Piece1Width, Piece1Height,
                                MathUtils.random(-300.0f, -30.0f), MathUtils.random(300.0f, 500.0f), MathUtils.random(-25.0f, 25.0f)));
                        AbstractDungeon.topLevelEffectsQueue.add(new FallingSpriteEffect(player.cX, player.cY,
                                MinigameSoul.renderColor, TextureLoader.getTexture(HeartPieceTexture2), Piece2Width, Piece2Height,
                                MathUtils.random(-190.0f, -80.0f), MathUtils.random(150.0f, 350.0f), MathUtils.random(-30.0f, 30.0f)));
                        AbstractDungeon.topLevelEffectsQueue.add(new FallingSpriteEffect(player.cX, player.cY,
                                MinigameSoul.renderColor, TextureLoader.getTexture(HeartPieceTexture3), Piece3Width, Piece3Height,
                                MathUtils.random(30.0f, 300.0f), MathUtils.random(200.0f, 450.0f), MathUtils.random(-20.0f, 20.0f)));
                        AbstractDungeon.topLevelEffectsQueue.add(new FallingSpriteEffect(player.cX, player.cY,
                                MinigameSoul.renderColor, TextureLoader.getTexture(HeartPieceTexture4), Piece4Width, Piece4Height,
                                MathUtils.random(60.0f, 190.0f), MathUtils.random(100.0f, 300.0f), MathUtils.random(-15.0f, 15.0f)));
                    }
                    break;
                case SHATTERED:
                    if (duration < 0)
                    {
                        deathState = DyingState.DONE;
                        AbstractDungeon.effectsQueue.add(new BlackScreenEffect(1.0f)); //to cover transition
                        this.isDone = true;
                    }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 771);
        sb.setColor(renderColor);

        if (!dying)
        {
            area.render(sb);

            float deltaTime = Gdx.graphics.getDeltaTime();

            for (AttackPattern p : attackPatterns)
            {
                p.render(deltaTime, sb, player, area);
            }
            player.render(sb, area);

            if (player.invincibleTime <= 0.0f)
            {
                for (AttackPattern p : attackPatterns)
                {
                    if (p.checkHit(player))
                    {
                        if (AbstractDungeon.player != null)
                        {
                            AbstractDungeon.player.damageFlash = true;
                            AbstractDungeon.player.damageFlashFrames = 4;
                            AbstractDungeon.player.currentHealth -= p.damageInfo.base;

                            if (AbstractDungeon.player.currentHealth <= 0) {
                                AbstractDungeon.player.currentHealth = 0;
                                dying = true;
                                deathState = DyingState.DYING;
                                this.duration = DYING_TIME;
                                MinigameSoul.renderColor = MinigameSoul.defaultColor;
                                if (AbstractDungeon.player.hasRelic(YourSoul.ID))
                                {
                                    AbstractDungeon.player.loseRelic(YourSoul.ID);
                                }

                                CardCrawlGame.music.dispose(); //Stop the bgm
                                //this.isDone = true;
                                CardCrawlGame.sound.play(SOUL_SPLIT.getKey());
                            } else if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth / 4) {
                                AbstractDungeon.topLevelEffectsQueue.add(new BorderFlashEffect(new Color(1.0F, 0.1F, 0.05F, 0.0F)));
                                player.invincibleTime = MinigameSoul.INVINCIBLE;
                                CardCrawlGame.sound.play(TAKE_DAMAGE.getKey());
                            }
                            else
                            {
                                player.invincibleTime = MinigameSoul.INVINCIBLE;
                                CardCrawlGame.sound.play(TAKE_DAMAGE.getKey());
                            }
                            AbstractDungeon.player.healthBarUpdatedEvent();

                            break;
                        }
                    }
                }
            }
        }
        else
        {
            sb.draw(black, 0, 0, 0, 0, Settings.WIDTH, Settings.HEIGHT, 1.0f, 1.0f, 0, 0, 0, 1, 1, false, false);
            if (deathState == DyingState.DYING)
            {
                player.render(sb, true);
            }
        }
    }

    @Override
    public void dispose() {
        for (AttackPattern p : attackPatterns)
        {
            p.dispose();
        }
        area.dispose();
    }
}
