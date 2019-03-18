package UnderSpire.Util;

import javafx.util.Pair;

import static UnderSpire.UnderSpire.assetPath;

public class Sounds {
    //Combat
    public static final Pair<String, String> TAKE_DAMAGE = new Pair<>("undertale:TAKE_DAMAGE", assetPath("audio/Damage.wav"));
    public static final Pair<String, String> NORMAL_ATTACK = new Pair<>("undertale:NORMAL_ATTACK", assetPath("audio/NormalAttack.wav"));
    public static final Pair<String, String> CRITICAL_ATTACK = new Pair<>("undertale:CRITICAL_ATTACK", assetPath("audio/CriticalAttack.wav"));
    public static final Pair<String, String> HEAL = new Pair<>("undertale:HEAL", assetPath("audio/Heal.wav"));
    public static final Pair<String, String> SOUL_SPLIT = new Pair<>("undertale:SOUL_SPLIT", assetPath("audio/SoulSplit.wav"));
    public static final Pair<String, String> SOUL_SHATTER = new Pair<>("undertale:SOUL_SHATTER", assetPath("audio/SoulShatter.wav"));

    //Notification Sounds
    public static final Pair<String, String> INTRO_NOISE = new Pair<>("undertale:INTRO_NOISE", assetPath("audio/mus_intronoise.ogg"));
    public static final Pair<String, String> LEVEL_UP = new Pair<>("undertale:LEVEL_UP", assetPath("audio/LoveUp.wav"));

    //Menu stuff
    public static final Pair<String, String> MENU_MOVE = new Pair<>("undertale:MENU_MOVE", assetPath("audio/MenuMove.wav"));
    public static final Pair<String, String> MENU_SELECT = new Pair<>("undertale:MENU_SELECT", assetPath("audio/MenuSelect.wav"));

    //Text sounds
    public static final Pair<String, String> NORMAL_TEXT = new Pair<>("undertale:NORMAL_TEXT", assetPath("audio/NormalText.wav"));
    public static final Pair<String, String> TEXT_A = new Pair<>("undertale:TEXT_A", assetPath("audio/Text2.wav"));
    public static final Pair<String, String> TEXT_ASGORE = new Pair<>("undertale:ASGORE", assetPath("audio/TextAsgore.wav"));

    //Music
    public static final Pair<String, String> GAME_OVER = new Pair<>("undertale:GAME_OVER", assetPath("audio/mus_gameover.ogg"));
    public static final Pair<String, String> DOG_SONG = new Pair<>("undertale:DOG_SONG", assetPath("audio/mus_dogsong.ogg"));
}
