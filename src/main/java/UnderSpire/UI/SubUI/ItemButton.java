package UnderSpire.UI.SubUI;

import UnderSpire.Abstracts.Undertale.UndertaleButton;
import UnderSpire.Patches.InventoryGroup;
import UnderSpire.UI.BattleUI;
import UnderSpire.Util.TextureLoader;
import UnderSpire.Util.UndertaleStringHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static UnderSpire.UnderSpire.battleUI;

public class ItemButton extends UndertaleButton {
    public ItemButton(float x, float y)
    {
        super(x, y, TextureLoader.getTexture(ITEM_TEXTURE), TextureLoader.getTexture(ITEM_SELECTED_TEXTURE));
    }

    @Override
    public void performAction() {
        if (InventoryGroup.Inventory.get(AbstractDungeon.player).size() > 0)
        {
            battleUI.setMenuText(UndertaleStringHelper.getItemOptionsText());
            battleUI.setMenuState(BattleUI.MenuState.ITEMS);
        }
    }
}