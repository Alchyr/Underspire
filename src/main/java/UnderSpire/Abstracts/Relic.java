package UnderSpire.Abstracts;

import UnderSpire.Util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static UnderSpire.UnderSpire.assetPath;

public abstract class Relic extends CustomRelic {
    @SuppressWarnings("all")
    public Relic(String setId, String textureID, RelicTier tier, LandingSound sfx) {
        super(setId, TextureLoader.getTexture(assetPath("img/Relics/") + textureID + ".png"), tier, sfx);
        outlineImg = TextureLoader.getTexture(assetPath("img/Relics/") + textureID + "-outline.png");
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        try{
            return this.getClass().newInstance();
        }catch(IllegalAccessException | InstantiationException e){
            throw new RuntimeException("Failed to auto-generate makeCopy for relic: " + this.relicId);
        }
    }
}
