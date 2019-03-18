package UnderSpire.Patches;


import UnderSpire.Interfaces.ArmorCard;
import UnderSpire.Interfaces.UndertalePlayer;
import UnderSpire.Interfaces.WeaponCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz = SoulGroup.class, method = "obtain")
public class ObtainEquipCardPatch {
    public static SpireReturn Prefix(SoulGroup __instance, AbstractCard card, boolean obtainCard)
    {
        if (card instanceof WeaponCard && AbstractDungeon.player instanceof UndertalePlayer)
        {
            ((UndertalePlayer) AbstractDungeon.player).equipWeapon((WeaponCard) card);
            return SpireReturn.Return(null);
        }
        if (card instanceof ArmorCard && AbstractDungeon.player instanceof UndertalePlayer)
        {
            ((UndertalePlayer) AbstractDungeon.player).equipArmor((ArmorCard) card);
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
