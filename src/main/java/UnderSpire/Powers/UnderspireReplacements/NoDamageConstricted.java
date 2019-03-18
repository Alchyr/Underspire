package UnderSpire.Powers.UnderspireReplacements;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

public class NoDamageConstricted extends AbstractPower {
    public static final String POWER_ID = "UnderSpire:NoDamageConstricted";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public AbstractCreature source;

    public NoDamageConstricted(AbstractCreature target, AbstractCreature source) {
        this.name = NAME;
        this.ID = ConstrictedPower.POWER_ID;
        this.owner = target;
        this.source = source;
        this.amount = -1;
        this.updateDescription();
        this.loadRegion("constricted");
        this.type = PowerType.DEBUFF;
        this.priority = 105;
    }

    @Override
    public void stackPower(int stackAmount) {
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_CONSTRICTED", 0.05F);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void atEndOfTurn(boolean isPlayer) {
        this.flashWithoutSound();
        this.playApplyPowerSfx();
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
