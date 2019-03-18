package UnderSpire.Patches;

import UnderSpire.Enums.CardColorEnum;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpirePatch(
        clz = OverlayMenu.class,
        method = "render"
)
public class NoOverlayPatch {
    @SpireInsertPatch(
            rloc=0
    )
    public static SpireReturn noRenderOverlay(OverlayMenu __instance, SpriteBatch sb)
    {
        if (AbstractDungeon.player.getCardColor() == CardColorEnum.DETERMINATION && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
        {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
