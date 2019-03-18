package UnderSpire.Abstracts.Undertale;

import UnderSpire.Enums.CardColorEnum;
import UnderSpire.Patches.SpareableEnemies;
import UnderSpire.UnderSpire;
import UnderSpire.Util.CardInfo;
import UnderSpire.Util.Fonts;
import UnderSpire.Util.TextureLoader;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.ExceptionHandler;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static UnderSpire.UnderSpire.makeID;

public abstract class BaseUndertaleCard extends CustomCard {
    private static final CardColor COLOR = CardColorEnum.DETERMINATION;

    private static final float DESC_OFFSET_X = 145.0F * Settings.scale;
    private static final float DESC_OFFSET_Y = 45.0F * Settings.scale;
    private static final float DESC_WIDTH = 280.0F * Settings.scale;

    protected CardStrings cardStrings = null;

    public BaseUndertaleCard(CardInfo cardInfo)
    {
        this(cardInfo.cardName, cardInfo.cardType, cardInfo.cardTarget, cardInfo.cardRarity);
    }

    public BaseUndertaleCard(String cardName, CardType cardType, CardTarget target, CardRarity rarity)
    {
        super(makeID(cardName), "", null, -2, "", cardType, COLOR, rarity, target);

        cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);

        this.textureImg = TextureLoader.getCardTextureString(cardName, cardType);
        loadCardImage(textureImg);

        this.rawDescription = cardStrings.DESCRIPTION;
        this.originalName = cardStrings.NAME;
        this.name = originalName;

        InitializeCard();
    }

    public void InitializeCard()
    {
        this.initializeTitle();
        this.initializeDescription();
    }

    public boolean canUpgrade() { return false; }
    public void upgrade() {}


    @Override
    public void render(SpriteBatch sb, boolean selected) {
        if (!Settings.hideCards) {

            if (!this.isFlipped)
            {
                this.renderCard(sb);
                this.hb.render(sb);
            }
            else
            {
                super.render(sb, selected);
            }
        }
    }

    @Override
    public void renderInLibrary(SpriteBatch sb) {
        if (this.isOnScreen()) {
            if (SingleCardViewPopup.isViewingUpgrade && this.isSeen && !this.isLocked) {
                AbstractCard copy = this.makeCopy();
                copy.current_x = this.current_x;
                copy.current_y = this.current_y;
                copy.drawScale = this.drawScale;
                copy.upgrade();
                copy.displayUpgrades();
                copy.render(sb);
            } else {
                this.renderCard(sb);
            }

        }
    }

    @Override
    public void renderHoverShadow(SpriteBatch sb) {
        //nope
    }

    private void renderCard(SpriteBatch sb) {
        if (!Settings.hideCards && this.isOnScreen()) {
            float drawX = this.current_x - 256.0F;
            float drawY = this.current_y - 256.0F;

            if (BaseMod.getPowerBgTexture(color) == null) {
                BaseMod.savePowerBgTexture(color, ImageMaster.loadImage(BaseMod.getPowerBg(color)));
            }
            Texture texture = BaseMod.getPowerBgTexture(color);

            if(!(this.textureBackgroundSmallImg == null) && !this.textureBackgroundSmallImg.isEmpty()) {
                texture = this.getBackgroundSmallTexture();
            }

            renderHelper(sb, Color.WHITE, texture, drawX, drawY);

            this.renderTitle(sb);

            renderDescriptionHelper(sb);
        }
    }

    private void renderDescriptionHelper(SpriteBatch sb)
    {
        if (Settings.lineBreakViaCharacter) {
            //renderDescriptionCN(sb);
        } else {
            renderDescription(sb);
        }
    }

    private void renderDescription(SpriteBatch sb)
    {
        Color textColor = Color.WHITE;

        if (this.isSeen && !this.isLocked) {
            if (this.angle >= -5.0f && this.angle < 5.0f)
            {
                BitmapFont font = Fonts.UndertaleMenuFont;

                font.getData().setScale(this.drawScale * 0.75f);

                float drawX = this.current_x - DESC_OFFSET_X * this.drawScale;
                float drawY = this.current_y - DESC_OFFSET_Y * this.drawScale;
                float width = DESC_WIDTH * this.drawScale;

                String description = rawDescription;
                if (isDamageModified)
                {
                    description = description.replace("!D!", Integer.toString(this.damage));
                }
                else
                {
                    description = description.replace("!D!", Integer.toString(this.baseDamage));
                }
                if (isBlockModified)
                {
                    description = description.replace("!B!", Integer.toString(this.block));
                }
                else
                {
                    description = description.replace("!B!", Integer.toString(this.baseBlock));
                }
                if (isMagicNumberModified)
                {
                    description = description.replace("!M!", Integer.toString(this.magicNumber));
                }
                else
                {
                    description = description.replace("!M!", Integer.toString(this.baseMagicNumber));
                }

                font.draw(sb, description, drawX, drawY, width, Align.topLeft, true);

                font.getData().setScale(1.0F);
            }
        } else {
            Fonts.UndertaleTextFont.getData().setScale(this.drawScale * 1.25F);
            FontHelper.renderRotatedText(sb, Fonts.UndertaleTextFont, "? ? ?", this.current_x, this.current_y, 0.0F, -200.0F * Settings.scale * this.drawScale / 2.0F, this.angle, true, textColor);
            Fonts.UndertaleTextFont.getData().setScale(Fonts.TEXT_FONT_DEFAULT_SCALE);
        }
    }

    private void renderTitle(SpriteBatch sb)
    {
        BitmapFont font = Fonts.UndertaleMenuFont;
        font.getData().setScale(this.drawScale);
        Color renderColor = Color.WHITE;
        if (this.isLocked) {
            FontHelper.renderRotatedText(sb, font, LOCKED_STRING, this.current_x, this.current_y, 0.0F, 0, this.angle, false, renderColor);
        } else if (!this.isSeen) {
            FontHelper.renderRotatedText(sb, font, UNKNOWN_STRING, this.current_x, this.current_y, 0.0F, 0, this.angle, false, renderColor);
        } else {
            FontHelper.renderRotatedText(sb, font, this.name, this.current_x, this.current_y, 0.0F, 0, this.angle, false, renderColor);
        }
    }

    private boolean isOnScreen() {
        return this.current_y >= -200.0F * Settings.scale && this.current_y <= (float)Settings.HEIGHT + 200.0F * Settings.scale;
    }

    private void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY, float scale) {
        sb.setColor(color);
        sb.draw(img, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle, 0, 0, 512, 512, false, false);
    }
    private void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY) {
        sb.setColor(color);
        try {
            sb.draw(img, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
        } catch (Exception e) {
            ExceptionHandler.handleException(e, UnderSpire.logger);
        }
    }
}
