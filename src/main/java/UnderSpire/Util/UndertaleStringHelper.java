package UnderSpire.Util;

import UnderSpire.Patches.InventoryGroup;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;

import java.util.ArrayList;

public class UndertaleStringHelper {
    private static final float DEFAULT_RATE = 0.035f;

    private static ArrayList<AbstractMonster> currentLiving = new ArrayList<>();
    private static EnemyMenuText currentEnemyOptions;
    private static MenuText currentItemOptions;
    private static MercyMenuText mercyOptions;

    public static UndertaleText getEnemyText(AbstractMonster m) {
        if (!ActInformation.activeActInformation.containsKey(m))
        {
            ActInformation.GenerateActInfo(m);
        }

        UndertaleText enemyText = new UndertaleText(new String[] { ActInformation.getStateText(m) }, DEFAULT_RATE);
        enemyText.permanentText = true;
        return enemyText;
    }
    public static UndertaleText getEnemyFirstTurnText(String groupName) {
        String[] options = ActInformation.getGroupFirstTurnStateText(groupName);

        if (options.length > 0)
        {
            UndertaleText enemyText = new UndertaleText(new String[] { options[MathUtils.random(0, options.length - 1)] }, DEFAULT_RATE);
            enemyText.permanentText = true;
            return enemyText;
        }
        return null;
    }

    public static EnemyMenuText getEnemyOptionsText() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<AbstractMonster> living = new ArrayList<>();

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            if (!m.isDeadOrEscaped())
            {
                living.add(m);
            }
        }

        if (currentEnemyOptions == null || !living.containsAll(currentLiving) || !currentLiving.containsAll(living))
        {
            currentLiving = living;
            String[] stringNames = new String[living.size()];
            for (int i = 0; i < stringNames.length; i++)
            {
                stringNames[i] = "* " + living.get(i).name;
            }
            currentEnemyOptions = new EnemyMenuText(stringNames, living);
        }

        return currentEnemyOptions;
    }

    public static MenuText getEnemyActOptions(AbstractMonster m)
    {
        if (ActInformation.activeActInformation.containsKey(m))
        {
            ActInformation info = ActInformation.activeActInformation.get(m);
            return info.actOptions[info.stateOptions[info.state]];
        }
        else
        {
            return null;
        }
    }
    public static UndertaleText getCheckInfo(AbstractMonster m)
    {
        if (ActInformation.activeActInformation.containsKey(m))
        {
            return new UndertaleText(new String[] { ActInformation.activeActInformation.get(m).checkInfo }, DEFAULT_RATE);
        }
        else
        {
            return new UndertaleText(new String[] { "" }, DEFAULT_RATE);
        }
    }

    public static MenuText getItemOptionsText() {
        ArrayList<String> names = new ArrayList<>();

        for (AbstractCard c : InventoryGroup.Inventory.get(AbstractDungeon.player).group)
        {
            names.add("* " + c.name);
        }

        String[] stringNames = new String[names.size()];
        for (int i = 0; i < stringNames.length; i++)
        {
            stringNames[i] = names.get(i);
        }
        currentItemOptions = new MenuText(stringNames);
        return currentItemOptions;
    }

    public static MercyMenuText getMercyOptionsText() {
        if (mercyOptions == null)
        {
            mercyOptions = new MercyMenuText();
        }
        return mercyOptions;
    }

    public static AbstractCard getSelectedItem(int index)
    {
        if (InventoryGroup.Inventory.get(AbstractDungeon.player) != null && index < InventoryGroup.Inventory.get(AbstractDungeon.player).group.size()) {
            return InventoryGroup.Inventory.get(AbstractDungeon.player).group.get(index);
        }
        return null;
    }
    public static AbstractMonster getSelectedMonster(int index)
    {
        if (currentLiving != null && index < currentLiving.size())
        {
            return currentLiving.get(index);
        }
        return null;
    }
}
