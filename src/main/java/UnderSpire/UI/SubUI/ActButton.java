package UnderSpire.UI.SubUI;

import UnderSpire.Abstracts.Undertale.UndertaleButton;
import UnderSpire.UI.BattleUI;
import UnderSpire.Util.UndertaleStringHelper;
import UnderSpire.Util.TextureLoader;

import static UnderSpire.UnderSpire.battleUI;

public class ActButton extends UndertaleButton {
    public ActButton(float x, float y)
    {
        super(x, y, TextureLoader.getTexture(ACT_TEXTURE), TextureLoader.getTexture(ACT_SELECTED_TEXTURE));
    }

    @Override
    public void performAction() {
        battleUI.setMenuText(UndertaleStringHelper.getEnemyOptionsText());
        battleUI.setMenuState(BattleUI.MenuState.ACT);
    }
}
