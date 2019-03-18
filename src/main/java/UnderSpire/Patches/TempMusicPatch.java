package UnderSpire.Patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;

import static UnderSpire.Util.Sounds.GAME_OVER;

@SpirePatch(
        clz = TempMusic.class,
        method = "getSong"
)
public class TempMusicPatch {
    @SpirePrefixPatch
    public static SpireReturn<Music> getSong(TempMusic __instance, String key)
    {
        if (key.equals(GAME_OVER.getKey()))
        {
            return SpireReturn.Return(MainMusic.newMusic(GAME_OVER.getValue()));
        }
        return SpireReturn.Continue();
    }
}
