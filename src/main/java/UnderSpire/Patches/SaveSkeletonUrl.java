package UnderSpire.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class SaveSkeletonUrl {
    @SpirePatch(
            clz = AbstractCreature.class,
            method = SpirePatch.CLASS
    )
    public static class SkeletonUrl
    {
        public static SpireField<String> SkeletonUrl = new SpireField<>(()->"");
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "loadAnimation"
    )
    public static class SaveTheUrl
    {
        @SpirePrefixPatch
        public static void SaveUrl(AbstractCreature __instance, String atlasUrl, String skeletonUrl, float scale)
        {
            SkeletonUrl.SkeletonUrl.set(__instance, skeletonUrl);
        }
    }
}
