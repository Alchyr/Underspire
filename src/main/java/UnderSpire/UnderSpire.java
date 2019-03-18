package UnderSpire;

import UnderSpire.Abstracts.Undertale.Attacks.AttackHandler;
import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.Actions.Undertale.EnemyAttackAction;
import UnderSpire.Attacks.Patterns.ChangingBox;
import UnderSpire.Character._____;
import UnderSpire.Enums.CardColorEnum;
import UnderSpire.Enums.CharacterEnum;
import UnderSpire.Enums.RewardEnum;
import UnderSpire.Interfaces.UndertalePlayer;
import UnderSpire.Relics.YourSoul;
import UnderSpire.Rewards.ExpReward;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.BattleUI;
import UnderSpire.UI.MinigameSoul;
import UnderSpire.Util.*;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javafx.util.Pair;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static UnderSpire.Util.Sounds.*;

@SpireInitializer
public class UnderSpire implements EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber,
        EditCharactersSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, AddAudioSubscriber,
        PreRenderSubscriber, RenderSubscriber
{
    //UnderSpire stuff
    private static Map<String, AttackHandler> enemyAttackHandlers;
    public static Map<String, String[]> textSoundMap;

    public static void addAttackHandler(AttackHandler toAdd)
    {
        enemyAttackHandlers.put(toAdd.id, toAdd);
    }
    public static void setDefaultAttackHandler(AttackHandler toAdd)
    {
        enemyAttackHandlers.put("", toAdd);
    }


    public static void generateEnemyAttack(ArrayList<AbstractGameAction> enemyActions)
    {
        Map<AbstractCreature, ArrayList<AbstractGameAction>> actions = new HashMap<>();

        int enemyCount = 0;

        for (AbstractGameAction action : enemyActions)
        {
            if (action.source != null)
            {
                if (!actions.containsKey(action.source))
                {
                    enemyCount++;
                    actions.put(action.source, new ArrayList<>());
                }
                actions.get(action.source).add(action);
            }
            else
            {
                if (action instanceof DamageAction)
                {
                    DamageInfo getInfo = (DamageInfo) ReflectionHacks.getPrivate(action, DamageAction.class, "info");
                    if (getInfo != null && getInfo.owner != null)
                    {
                        if (!actions.containsKey(getInfo.owner))
                        {
                            enemyCount++;
                            actions.put(getInfo.owner, new ArrayList<>());
                        }
                        action.amount = getInfo.base;
                        action.damageType = getInfo.type;

                        actions.get(getInfo.owner).add(action);
                    }
                }
            }
        }


        ArrayList<AttackPattern> patterns = new ArrayList<>();
        BattleArea minigameArea = new BattleArea(200, 200);

        //in switch case can add "PreferredSize" paired with "Priority"
        //Then decide final size based on highest priority PreferredSize


        for (Map.Entry<AbstractCreature, ArrayList<AbstractGameAction>> entry : actions.entrySet())
        {
            if (enemyAttackHandlers.containsKey(entry.getKey().id))
            {
                patterns.addAll(enemyAttackHandlers.get(entry.getKey().id).getAttackPattern(entry.getKey(), entry.getValue(), enemyCount));
            }
            else if (enemyAttackHandlers.containsKey(""))
            {
                patterns.addAll(enemyAttackHandlers.get("").getAttackPattern(entry.getKey(), entry.getValue(), enemyCount));
            }
        }

        /*
        for (Map.Entry<AbstractCreature, ArrayList<AbstractGameAction>> entry : actions.entrySet())
        {
            if (entry.getKey() != null && entry.getKey().id != null)
            {
                switch (entry.getKey().id)
                {
                    case SpireGrowth.ID:
                        for (AbstractGameAction a : entry.getValue())
                        {
                            if (a.actionType == AbstractGameAction.ActionType.DAMAGE)
                            {
                                //two attacks, one stronger one weak, scale by damage
                            }
                            else if (a instanceof ApplyPowerAction)
                            {
                                if (!a.target.hasPower(NoDamageConstricted.POWER_ID))
                                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(a.target, null, new NoDamageConstricted(a.target, a.source), 1));
                            }
                        }
                        break;
                }
            }
        }*/


        //get other special attack effects
        if (AbstractDungeon.player.hasPower(ConstrictedPower.POWER_ID))
        {
            patterns.add(new ChangingBox(0.5f, 5.0f));
        }


        if (!patterns.isEmpty())
        {
            ArrayList<Pair<Integer, Pair<Integer, Integer>>> suggestedAreas = new ArrayList<>();

            Pair<Integer, Integer> highestPriority = null;
            int priority = -1;

            for (AttackPattern p : patterns)
            {
                Pair<Integer, Pair<Integer, Integer>> suggestedArea = p.preferredSize();
                if (suggestedArea != null)
                {
                    if (suggestedArea.getKey() > priority)
                    {
                        priority = suggestedArea.getKey();
                        highestPriority = suggestedArea.getValue();
                    }
                }
            }

            if (highestPriority != null)
            {
                minigameArea.dispose(); //dispose default area
                minigameArea = new BattleArea(highestPriority.getKey(), highestPriority.getValue());
            }
            AbstractDungeon.actionManager.addToBottom(new EnemyAttackAction(minigameArea, new MinigameSoul(), patterns, 7.0f));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new EnemyAttackAction(minigameArea, new MinigameSoul(), patterns, 1.5f));
        }
    }




    //UI
    public static BattleUI battleUI;

    @Override
    public void receiveCameraRender(OrthographicCamera orthographicCamera) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.player != null)
        {
            if (AbstractDungeon.player instanceof UndertalePlayer && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.currentHealth > 0 && !AbstractDungeon.player.isDead)
            {
                battleUI.update(deltaTime);
            }
        }
    }

    @Override
    public void receiveRender(SpriteBatch spriteBatch) {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.player != null)
        {
            if (AbstractDungeon.player instanceof UndertalePlayer && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.currentHealth > 0 && !AbstractDungeon.player.isDead)
            {
                battleUI.render(spriteBatch);
            }
        }
    }

    //Mod setup stuff
    public static final Logger logger = LogManager.getLogger("Undertale");

    // Character color
    public static final Color HEART = CardHelper.getColor(255.0f, 0.0f, 0.0f);
    public static final Color EMPTINESS = CardHelper.getColor(0.0f, 0.0f, 0.0f);

    // Mod panel stuff
    private static final String BADGE_IMAGE = "img/Badge.png";
    private static final String MODNAME = "UnderSpire";
    private static final String AUTHOR = "Alchyr"; // And pretty soon - You!
    private static final String DESCRIPTION = "Slay the Spire, but it's a completely different game.";

    // Card backgrounds/basic images
    private static final String ATTACK_BACK = "img/Character/CardGeneric/bg_attack.png";
    private static final String POWER_BACK = "img/Character/CardGeneric/bg_power.png";
    private static final String SKILL_BACK = "img/Character/CardGeneric/bg_skill.png";
    private static final String ENERGY_ORB = "img/Character/CardGeneric/card_orb.png";
    private static final String CARD_ENERGY_ORB = "img/Character/CardGeneric/card_small_orb.png";

    private static final String ATTACK_PORTRAIT = "img/Character/CardGeneric/portrait_attack.png";
    private static final String POWER_PORTRAIT = "img/Character/CardGeneric/portrait_power.png";
    private static final String SKILL_PORTRAIT = "img/Character/CardGeneric/portrait_skill.png";
    private static final String CARD_ENERGY_ORB_PORTRAIT = "img/Character/CardGeneric/card_large_orb.png";

    // Character images
    private static final String BUTTON = "img/Character/CharacterButton.png";
    private static final String PORTRAIT = "img/Character/CharacterPortrait.png";

    public UnderSpire()
    {
        for (int i = 0; i < 10; i++)
        {
            logger.info("Exception occured while loading \"file0\": Location \"UNDERGROUND\" not found.");
            logger.info("Retrying...");
            logger.info("..");
            logger.info(".");
            logger.info("...");
            if (MathUtils.randomBoolean())
                break;
            logger.info("..");
            if (MathUtils.randomBoolean())
                break;
            logger.info(".");
        }
        BaseMod.subscribe(this);

        logger.info("Location definition failed. Loading DefaultLocation.");
        for (int i = 0; i < 12; i++)
        {
            logger.info("...");
            if (MathUtils.randomBoolean())
                break;
            logger.info("..");
            if (MathUtils.randomBoolean())
                break;
            logger.info(".");
            if (MathUtils.randomBoolean())
                break;
            logger.info("...");
            if (MathUtils.randomBoolean())
                break;
            logger.info("..");
            if (MathUtils.randomBoolean())
                break;
            logger.info(".");
        }
        logger.info("DefaultLocation found: \"SPIRE_BASE\". Initializing...");
        BaseMod.addColor(CardColorEnum.DETERMINATION, HEART,
                assetPath(ATTACK_BACK), assetPath(SKILL_BACK), assetPath(POWER_BACK),
                assetPath(ENERGY_ORB),
                assetPath(ATTACK_PORTRAIT), assetPath(SKILL_PORTRAIT), assetPath(POWER_PORTRAIT),
                assetPath(CARD_ENERGY_ORB_PORTRAIT), assetPath(CARD_ENERGY_ORB));
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new _____(_____.characterStrings.NAMES[1], CharacterEnum._____),
                assetPath(BUTTON), assetPath(PORTRAIT), CharacterEnum._____);
    }

    @Override
    public void receiveEditCards() {
        try {
            autoAddCards();
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveAddAudio() {
        textSoundMap = new HashMap<>();

        //Combat
        addAudio(TAKE_DAMAGE);
        addAudio(NORMAL_ATTACK);
        addAudio(CRITICAL_ATTACK);
        addAudio(HEAL);
        addAudio(SOUL_SPLIT);
        addAudio(SOUL_SHATTER);

        //SFX
        addAudio(INTRO_NOISE);
        addAudio(LEVEL_UP);

        //Menuing
        addAudio(MENU_MOVE);
        addAudio(MENU_SELECT);

        //Text
        addAudio(NORMAL_TEXT);
        addAudio(TEXT_A);
        addAudio(TEXT_ASGORE);

        textSoundMap.put("NORMAL_TEXT", new String[] {
                NORMAL_TEXT.getKey(),
        });
        textSoundMap.put("ASGORE", new String[] {
                TEXT_ASGORE.getKey()
        });

        //Other sounds
        addAudio(GAME_OVER);
        addAudio(DOG_SONG);
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.registerCustomReward(
                RewardEnum.UNDERTALE_EXECUTION_POINTS,
                (rewardSave) -> new ExpReward(rewardSave.amount),
                (customReward) -> new RewardSave(customReward.type.toString(), null, ((ExpReward)customReward).amount, 0));

        Fonts.Initialize();
        AttackHandler.InitializeHandlers();

        //Setup mod menu info stuff
        Texture badgeTexture = TextureLoader.getTexture(assetPath(BADGE_IMAGE));

        if (badgeTexture != null)
        {
            ModPanel panel = new ModPanel();

            BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, panel);
        }



        //setup UI elements
        battleUI = new BattleUI();
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new YourSoul(), CardColorEnum.DETERMINATION);
    }

    @Override
    public void receiveEditStrings()
    {
        String lang = getLangString();

        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath("localization/" + lang + "/RelicStrings.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath("localization/" + lang + "/CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, assetPath("localization/" + lang + "/CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath("localization/" + lang + "/PowerStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath("localization/" + lang + "/UIStrings.json"));

        UndertaleText.Initialize(assetPath("localization/" + lang + "/UndertaleStrings.json"));

        ActInformation.Initialize(lang);
    }

    @Override
    public void receiveEditKeywords()
    {
        String lang = getLangString();

        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath("localization/" + lang + "/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordWithProper[] keywords = gson.fromJson(json, KeywordWithProper[].class);

        if (keywords != null) {
            for (KeywordWithProper keyword : keywords) {
                BaseMod.addKeyword("UnderSpire", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    private String getLangString()
    {
        switch (Settings.language) {
            default:
                return "eng";
        }
    }

    private void addAudio(Pair<String, String> audioData)
    {
        BaseMod.addAudio(audioData.getKey(), audioData.getValue());
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        enemyAttackHandlers = new HashMap<>();
        new UnderSpire();
    }


    //I totally didn't copy this from Hubris, made by kiooeht.
    private static void autoAddCards() throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, CannotCompileException
    {
        ClassFinder finder = new ClassFinder();
        URL url = UnderSpire.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter =
                new AndClassFilter(
                        new NotClassFilter(new InterfaceOnlyClassFilter()),
                        new NotClassFilter(new AbstractClassFilter()),
                        new ClassModifiersClassFilter(Modifier.PUBLIC),
                        new CardFilter()
                );
        Collection<ClassInfo> foundClasses = new ArrayList<>();
        ArrayList<AbstractCard> addedCards = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            CtClass cls = Loader.getClassPool().get(classInfo.getClassName());

            boolean isCard = false;
            CtClass superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.getName().equals(AbstractCard.class.getName())) {
                    isCard = true;
                    break;
                }
            }
            if (!isCard) {
                continue;
            }

            AbstractCard card = (AbstractCard) Loader.getClassPool().toClass(cls).newInstance();

            BaseMod.addCard(card);
            addedCards.add(card);

        }
        for (AbstractCard c : addedCards)
        {
            UnlockTracker.unlockCard(c.cardID);
        }
    }


    public static String makeID(String partialID)
    {
        return "UnderSpire:" + partialID;
    }
    public static String assetPath(String partialPath)
    {
        return "UnderSpire/" + partialPath;
    }
}
