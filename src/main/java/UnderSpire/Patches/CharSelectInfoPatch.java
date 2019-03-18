package UnderSpire.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;

@SpirePatch(
        clz = CharSelectInfo.class,
        method = "createDeckInfoString"
)
public class CharSelectInfoPatch {
    @SpirePrefixPatch
    public static SpireReturn<String> preventEmptyDeckCrash(ArrayList<String> deck)
    {
        if (deck.size() == 0)
        {
            return SpireReturn.Return("");
        }
        return SpireReturn.Continue();
    }
}