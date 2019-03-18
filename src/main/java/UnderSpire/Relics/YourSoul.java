package UnderSpire.Relics;

import UnderSpire.Abstracts.Relic;
import UnderSpire.Actions.Undertale.LevelUpAction;
import UnderSpire.Interfaces.ModifyUndertaleAttack;
import UnderSpire.Interfaces.UndertalePlayer;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static UnderSpire.UnderSpire.makeID;
import static UnderSpire.Util.ActInformation.activeActInformation;

public class YourSoul extends Relic implements ModifyUndertaleAttack {
    public static final String ID = makeID("YourSoul");

    public int level;

    public int[] xpAmounts;

    public boolean updateTooltip;

    public YourSoul()
    {
        super(ID, "YourSoul",
                RelicTier.STARTER, LandingSound.MAGICAL);
        this.setCounter(0);
        this.updateTooltip = true;

        xpAmounts = new int[] {
                0,
                10,
                30,
                70,
                120,
                200,
                300,
                500,
                800,
                1200,
                1700,
                2500,
                3500,
                5000,
                7000,
                10000,
                15000,
                25000,
                50000,
                99999
        };
    }

    @Override
    public int modifyAttack(int attack) {
        return attack + getLevelDamage();
    }

    @Override
    public void atBattleStartPreDraw() {
        activeActInformation.clear(); //Each monster is assigned act information when first attempted to act on
        //Rather than assigning at start of battle
        //This is to ensure all enemies do end up with information, including minions
    }

    @Override
    public void atTurnStart() {
        UnderSpire.UnderSpire.battleUI.turnStartText();
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth <= 0) {
            addXP(10);
            //Later, load all monster xp values from a json and get from there
        }
    }

    public void addXP(int amount)
    {
        counter += amount;
        updateTooltip = true;

        while (getNextLevelXP() <= 0)
        {
            level++;
            AbstractDungeon.actionManager.addToBottom(new LevelUpAction(level, true));
        }
    }

    private int calculateLevel() {
        int level = 0;
        for (int xpReq : xpAmounts)
        {
            if (counter < xpReq)
            {
                break;
            }
            level++;
        }
        return level;
    }
    private int getNextLevelXP() {
        if (level < xpAmounts.length)
        {
            return xpAmounts[level] - counter;
        }
        return 0;
    }
    private int getLevelDamage() {
        return 8 + level * 2;
    }
    private int getLevelDefense() {
        int defenseLevel = MathUtils.floor((level - 1) / 4.0f); //1-4 = 0, 5-8 = 1, etc
        return 10 + defenseLevel;
    }

    @Override
    public void update() {
        if (counter > xpAmounts[xpAmounts.length - 1])
        {
            counter = xpAmounts[xpAmounts.length - 1];
        }
        level = calculateLevel(); //to ensure after loading value is correct; does not need to do actual leveling up since that would add hp (which is saved)
        if (updateTooltip)
        {
            String name = System.getProperty("user.name");
            //Later, make this not hardcoded. Set this stuff up in relic strings.
            AbstractCard weapon = null;
            AbstractCard armor = null;
            int weaponAttack = 0;
            int armorDefense = 0;
            if (AbstractDungeon.player instanceof UndertalePlayer)
            {
                if (((UndertalePlayer) AbstractDungeon.player).getEquippedWeapon() != null)
                {
                    weapon = ((UndertalePlayer) AbstractDungeon.player).getEquippedWeaponAsCard();
                    weaponAttack = ((UndertalePlayer) AbstractDungeon.player).getEquippedWeapon().getBaseDamage();
                }
                if (((UndertalePlayer) AbstractDungeon.player).getEquippedArmor() != null)
                {
                    armor = ((UndertalePlayer) AbstractDungeon.player).getEquippedArmorAsCard();
                    armorDefense = ((UndertalePlayer) AbstractDungeon.player).getEquippedArmor().getDefense();
                }
            }

            this.description = "\"" + name + "\" NL NL ";
            this.description += "LV " + level + " NL NL ";
            this.description += "AT: " + getLevelDamage() + " " + (weapon != null ? "(" + weaponAttack + ")" : "") + " NL ";
            this.description += "DF: " + getLevelDefense() + " " + (armor != null ? "(" + armorDefense + ")" : "") + " NL  NL ";
            this.description += "Weapon: " + (weapon != null ? weapon.name : "None") + " NL ";
            this.description += "Armor: " + (armor != null ? armor.name : "None") + " NL NL ";
            this.description += "EXP: " + counter;
            if (level < xpAmounts.length)
            {
                this.description += " NL NEXT: " + getNextLevelXP();
            }
            updateTooltip = false;

            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }
        super.update();
    }
}
