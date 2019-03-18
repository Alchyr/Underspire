package UnderSpire.Rewards;

import UnderSpire.Enums.RewardEnum;
import UnderSpire.Relics.YourSoul;
import UnderSpire.Util.TextureLoader;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static UnderSpire.UnderSpire.assetPath;

public class ExpReward extends CustomReward {
    private static final Texture ICON = TextureLoader.getTexture(assetPath("img/Rewards/Execution.png"));

    private static final String RewardID = "UnderSpire:EXPReward";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(RewardID);

    public int amount;

    public ExpReward(int amount)
    {
        super(ICON, uiStrings.TEXT[0] + amount + uiStrings.TEXT[1], RewardEnum.UNDERTALE_EXECUTION_POINTS);

        this.amount = amount;
    }

    @Override
    public boolean claimReward() {
        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof YourSoul)
            {
                ((YourSoul) r).addXP(this.amount);
                return true;
            }
        }
        return false;
    }
}
