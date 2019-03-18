package UnderSpire.UI.SubUI;

import UnderSpire.Abstracts.Undertale.UndertaleButton;
import UnderSpire.UI.BattleUI;
import UnderSpire.Util.TextureLoader;
import UnderSpire.Util.UndertaleStringHelper;

import static UnderSpire.UnderSpire.battleUI;

public class MercyButton extends UndertaleButton {
    public MercyButton(float x, float y)
    {
        super(x, y, TextureLoader.getTexture(MERCY_TEXTURE), TextureLoader.getTexture(MERCY_SELECTED_TEXTURE));
    }

    @Override
    public void performAction() {
        battleUI.setMenuText(UndertaleStringHelper.getMercyOptionsText());
        battleUI.setMenuState(BattleUI.MenuState.MERCY);
    }
}