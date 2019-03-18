package UnderSpire.Character;

import UnderSpire.Abstracts.Undertale.ItemCard;
import UnderSpire.Interfaces.*;
import UnderSpire.Cards.Starter.Bandage;
import UnderSpire.Cards.Starter.Stick;
import UnderSpire.EnergyOrb.InvisibleEnergyOrb;
import UnderSpire.Enums.CardColorEnum;
import UnderSpire.Relics.YourSoul;
import UnderSpire.Util.TextureLoader;
import UnderSpire.Util.WeaponHits;
import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.abstracts.CustomSavable;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.RemoveNextOrbAction;
import com.megacrit.cardcrawl.actions.unique.DualWieldAction;
import com.megacrit.cardcrawl.actions.unique.TempestAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Dualcast;
import com.megacrit.cardcrawl.cards.blue.Zap;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static UnderSpire.UnderSpire.assetPath;
import static UnderSpire.Util.Sounds.INTRO_NOISE;

public class _____ extends CustomPlayer implements UndertalePlayer, CustomSavable<String> {
    public static final Logger logger = LogManager.getLogger(_____.class.getName());

    public WeaponCard equippedWeapon;
    public ArmorCard equippedArmor;

    @Override
    public String onSave() {
        String weaponID = "";
        String armorID = "";

        if (equippedWeapon instanceof AbstractCard) //automatic null check
        {
            weaponID = ((AbstractCard) equippedWeapon).cardID;
        }
        if (equippedArmor instanceof AbstractCard)
        {
            armorID = ((AbstractCard) equippedArmor).cardID;
        }
        return weaponID + "!-!-!" + armorID;
    }

    @Override
    public void onLoad(String equipmentIDs) {
        if (equipmentIDs != null && equipmentIDs.contains("!-!-!"))
        {
            String[] equipment = equipmentIDs.split("!-!-!");
            if (equipment.length == 2)
            {
                if (!equipment[0].equals(""))
                {
                    AbstractCard weapon = CardLibrary.getCopy(equipment[0]);
                    if (weapon instanceof WeaponCard)
                        equippedWeapon = (WeaponCard)weapon;
                }
                if (!equipment[1].equals(""))
                {
                    AbstractCard armor = CardLibrary.getCopy(equipment[1]);
                    if (armor instanceof ArmorCard)
                        equippedArmor = (ArmorCard)armor;
                }
            }
        }
    }

    @Override
    public Type savedType() {
        return String.class;
    }

    @Override
    public void equipWeapon(WeaponCard toEquip)
    {
        if (equippedWeapon instanceof ItemCard)
        {
            masterDeck.addToBottom((ItemCard) equippedWeapon);
        }

        equippedWeapon = toEquip;
        for (AbstractRelic r : relics)
        {
            if (r instanceof YourSoul)
            {
                ((YourSoul) r).updateTooltip = true;
            }
        }
    }
    @Override
    public void equipArmor(ArmorCard toEquip)
    {
        if (equippedArmor instanceof ItemCard)
        {
            masterDeck.addToBottom((ItemCard) equippedArmor);
        }

        equippedArmor = toEquip;
        for (AbstractRelic r : relics)
        {
            if (r instanceof YourSoul)
            {
                ((YourSoul) r).updateTooltip = true;
            }
        }
    }

    @Override
    public AbstractCard getEquippedWeaponAsCard()
    {
        if (equippedWeapon instanceof AbstractCard)
            return (AbstractCard)equippedWeapon;
        return null;
    }
    @Override
    public WeaponCard getEquippedWeapon()
    {
        return equippedWeapon;
    }

    @Override
    public AbstractCard getEquippedArmorAsCard()
    {
        if (equippedArmor instanceof AbstractCard)
            return (AbstractCard)equippedArmor;
        return null;
    }
    @Override
    public ArmorCard getEquippedArmor()
    {
        return equippedArmor;
    }

    @Override
    public WeaponHits getWeaponHits()
    {
        if (equippedWeapon != null)
        {
            return equippedWeapon.getWeaponHits();
        }
        return new WeaponHits(MathUtils.random(1, 4), MathUtils.random(200.0f, 350.0f), 0.15f, 0.5f, WeaponHits.DirectionPreference.EITHER);
    }

    @Override
    public void performAttack(WeaponHits hits) { //some of these should be multi-hits?
        DamageInfo attackInfo = getAttackInfo();
        attackInfo.output = MathUtils.floor(attackInfo.base * hits.getDamageModifier());
        AbstractDungeon.actionManager.addToBottom(new DamageAction(hits.target, attackInfo, doAttackEffect(hits.critical)));
    }

    @Override
    public DamageInfo getAttackInfo() {
        int damage = 0;
        if (equippedWeapon != null)
        {
            damage = equippedWeapon.getBaseDamage();
        }
        for (AbstractRelic r : relics)
        {
            if (r instanceof ModifyUndertaleAttack)
            {
                damage = ((ModifyUndertaleAttack) r).modifyAttack(damage);
            }
        }
        for (AbstractPower p : powers)
        {
            if (p instanceof ModifyUndertaleAttack)
            {
                damage = ((ModifyUndertaleAttack) p).modifyAttack(damage);
            }
            else if (p instanceof StrengthPower)
            {
                damage += p.amount;
            }
        }
        return new DamageInfo(this, damage, DamageInfo.DamageType.NORMAL);
        //Make damage scale based on target's missing health?
        //Somewhat similar to how damage often increases on weaker enemies in undertale,
        //and helps reduce drag of fights with large amounts of hp
    }

    @Override
    public AbstractGameAction.AttackEffect doAttackEffect(boolean critical) {
        if (equippedWeapon != null)
        {
            return equippedWeapon.doAttackEffect(critical);
        }
        else
        {
            if (critical)
            {
                return AbstractGameAction.AttackEffect.BLUNT_HEAVY;
            }
            else
            {
                return AbstractGameAction.AttackEffect.BLUNT_LIGHT;
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        //modify output based on defense
        float defenseValue = 0;
        if (equippedArmor != null) {
            defenseValue = equippedArmor.getDefense();
        }

        for (AbstractRelic r : relics) {
            if (r instanceof ModifyUndertaleDefense) {
                defenseValue = ((ModifyUndertaleDefense) r).modifyDefense(defenseValue);
            }
        }

        info.output = MathUtils.round(info.output * (1.0f - defenseValue / 100.0f));

        super.damage(info);
    }


    // Normal character class stuff

    public static final com.megacrit.cardcrawl.localization.CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("_____:_____");

    // =============== BASE STATS =================

    private static final int ENERGY_PER_TURN = 999;
    private static final int STARTING_HP = 20;
    private static final int MAX_HP = 20;
    private static final int STARTING_GOLD = 0;
    private static final int CARD_DRAW = 10;
    private static final int ORB_SLOTS = 0;

    // =============== TEXTURES OF BIG ENERGY ORB ===============

    private static final String[] orbTextures = {
            assetPath("img/Character/orb/layer1.png"),
            assetPath("img/Character/orb/layer2.png"),
            assetPath("img/Character/orb/layer3.png"),
            assetPath("img/Character/orb/layer4.png"),
            assetPath("img/Character/orb/layer5.png"),
            assetPath("img/Character/orb/layer6.png"),
            assetPath("img/Character/orb/layer1d.png"),
            assetPath("img/Character/orb/layer2d.png"),
            assetPath("img/Character/orb/layer3d.png"),
            assetPath("img/Character/orb/layer4d.png"),
            assetPath("img/Character/orb/layer5d.png") };

    private static final String VFXTexture = assetPath("img/Character/orb/vfx.png");

    private static final String SpritePath = assetPath("img/Character/Spriter/Character.scml");

    private static final float[] layerSpeeds = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F};

    private static final Color cardRenderColor = Color.WHITE.cpy();
    private static final Color cardTrailColor = Color.RED.cpy();
    private static final Color slashAttackColor = Color.WHITE.cpy();


    private static Texture blankTexture = null;

    public _____(String name, PlayerClass setClass) {
        super(name, setClass, new InvisibleEnergyOrb(),
                new SpriterAnimation(SpritePath));


        // =============== TEXTURES, ENERGY, LOADOUT =================

        initializeClass(null, // required call to load textures and setup energy/loadout
                assetPath("img/Character/shoulder.png"), // campfire pose
                assetPath("img/Character/shoulder2.png"), // another campfire pose
                assetPath("img/Character/corpse.png"), // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        if (blankTexture == null)
        {
            blankTexture = TextureLoader.getTexture(assetPath("img/Blank.png"));
        }

        // =============== TEXT BUBBLE LOCATION =================

        this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
        this.dialogY = (this.drawY + 120.0F * Settings.scale); // you can just copy these values

        // SAVE EQUIPMENT
        BaseMod.addSaveField("UnderSpire:CharacterEquipment", this);
    }


    public Texture getEnergyImage() {
        return blankTexture;
    }


    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], characterStrings.TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> startDeck = new ArrayList<>();

        this.equippedWeapon = new Stick();
        this.equippedArmor = new Bandage();

        return startDeck;
    }

    // Starting Relics
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> startingRelics = new ArrayList<>();

        startingRelics.add(YourSoul.ID);

        return startingRelics;
    }



    // Character select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA(INTRO_NOISE.getKey(), 0.0f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // Character select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return INTRO_NOISE.getKey();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return CardColorEnum.DETERMINATION;
    }


    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }
    @Override
    public AbstractCard getStartCardForEvent() {
        return null;
    }
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return characterStrings.NAMES[1];
    }


    @Override
    public AbstractPlayer newInstance() {
        return new _____(this.name, this.chosenClass);
    }

    @Override
    public Color getCardTrailColor() {
        return cardTrailColor;
    }
    @Override
    public Color getCardRenderColor() {
        return cardRenderColor;
    }
    @Override
    public Color getSlashAttackColor() {
        return slashAttackColor;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.NONE
        };
    }

    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }
    @Override
    public String getVampireText() {
        return characterStrings.TEXT[2];
    }
}