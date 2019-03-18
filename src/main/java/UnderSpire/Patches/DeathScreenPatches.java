package UnderSpire.Patches;

import UnderSpire.Enums.CardColorEnum;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import javassist.CtBehavior;

import static UnderSpire.Util.Sounds.GAME_OVER;

@SpirePatch(
        clz = DeathScreen.class,
        method = SpirePatch.CONSTRUCTOR
)
public class DeathScreenPatches {
    private static float currentVol = 1.0f;
    @SpireInsertPatch(
            locator = FirstLocator.class
    )
    public static void Insert(DeathScreen __instance, MonsterGroup m)
    {
        if (AbstractDungeon.player.getCardColor() == CardColorEnum.DETERMINATION)
        {
            currentVol = Settings.MUSIC_VOLUME;
            Settings.MUSIC_VOLUME = 0.0f; //Prevent normal death sound from playing
        }
    }

    @SpireInsertPatch(
            locator = SecondLocator.class,
            localvars = { "bgmKey" }
    )
    public static void SecondInsert(DeathScreen __instance, MonsterGroup m, @ByRef(type="java.lang.String") Object[] bgmKey)
    {
        if (AbstractDungeon.player.getCardColor() == CardColorEnum.DETERMINATION) {
            Settings.MUSIC_VOLUME = currentVol;
            bgmKey[0] = GAME_OVER.getKey();
        }
    }

    private static class FirstLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(SoundMaster.class, "play");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
    private static class SecondLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MusicMaster.class, "playTempBgmInstantly");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
