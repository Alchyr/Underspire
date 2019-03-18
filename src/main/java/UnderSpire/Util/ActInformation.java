package UnderSpire.Util;

import UnderSpire.Actions.Undertale.TextBoxAction;
import UnderSpire.Actions.Undertale.WaitForTextDoneAction;
import UnderSpire.Patches.SpareableEnemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static UnderSpire.UnderSpire.assetPath;
import static UnderSpire.UnderSpire.logger;

public class ActInformation {
    public static HashMap<String, BaseActInformation> ActInformation;
    public static HashMap<String, String[]> FirstTurnStateText;

    private static final String ACT_INFORMATION = "/ActInformation.json";
    private static final String FIRST_TURN_INFO = "/FirstTurnText.json";

    public static HashMap<AbstractMonster, ActInformation> activeActInformation;

    public static void Initialize(String lang)
    {
        try
        {
            String file = assetPath("localization/" + lang + ACT_INFORMATION);

            Gson gson = new Gson();
            String json = Gdx.files.internal(file).readString(String.valueOf(StandardCharsets.UTF_8));

            //Act information
            Type type = new TypeToken<Map<String, BaseActInformation>>(){}.getType();
            Map<String, BaseActInformation> map = gson.fromJson(json, type);

            ActInformation = new HashMap<>();
            for (Map.Entry<String, BaseActInformation> entry : map.entrySet())
            {
                ActInformation.put(entry.getKey(), entry.getValue());
            }

            //First turn state text
            file = assetPath("localization/" + lang + FIRST_TURN_INFO);

            json = Gdx.files.internal(file).readString(String.valueOf(StandardCharsets.UTF_8));

            type = new TypeToken<Map<String, String[]>>(){}.getType();
            Map<String, String[]> initialStateTextMap = gson.fromJson(json, type);

            FirstTurnStateText = new HashMap<>();
            for (Map.Entry<String, String[]> entry : initialStateTextMap.entrySet())
            {
                FirstTurnStateText.put(entry.getKey(), entry.getValue());
            }

            activeActInformation = new HashMap<>();
        }
        catch (Exception e)
        {
            logger.info("Loading Undertale act strings failed.");
        }
    }

    public static void GenerateActInfo(AbstractMonster m)
    {
        activeActInformation.remove(m);

        if (ActInformation.containsKey(m.id))
        {
            ActInformation actInfo = new ActInformation(ActInformation.get(m.id), m);

            activeActInformation.put(m, actInfo);
        }
        else
        {
            ActInformation actInfo = new ActInformation(m);

            activeActInformation.put(m, actInfo);
        }
    }

    public static String GenerateCheckInfo(AbstractMonster m)
    {
        String info = m.name.toUpperCase() + " - ATK ??? DEF ???\n";
        info += "You have no idea what this is supposed to be.";

        return info;
    }

    public static String[][] GenerateStateTexts(AbstractMonster m)
    {
        String[][] stateTexts = new String[1][1];

        stateTexts[0][0] = "It's a " + m.name + "! How fascinating.";

        return stateTexts;
    }

    public static String getStateText(AbstractMonster m)
    {
        if (activeActInformation.containsKey(m))
        {
            return activeActInformation.get(m).stateTexts[activeActInformation.get(m).state][MathUtils.random(0, activeActInformation.get(m).stateTexts[activeActInformation.get(m).state].length - 1)];
        }
        else
        {
            return "It's a " + m.name + "! How fascinating.";
        }
    }
    public static String[] getGroupFirstTurnStateText(String groupName)
    {
        if (FirstTurnStateText.containsKey(groupName))
        {
            return FirstTurnStateText.get(groupName);
        }
        return new String[] { };
    }

    public String checkInfo;
    public MenuText[] actOptions; //Arraylist based on state
    public int[] stateOptions; //The act options/state changes linked to a certain state (to avoid repetition of same things)
    public int minSparableState; //Any state >= this state is sparable
    public String[][] actTexts; //Text that appears when doing certain act options; IDs of UndertaleStrings
    public int[][] stateChanges; //Array of Array of state changes
    public String[][] stateTexts; //What shows up at start of turn based on current state, randomly chosen from array

    public int state;

    public AbstractMonster monster;

    public ActInformation(BaseActInformation baseInfo, AbstractMonster m)
    {
        this.checkInfo = baseInfo.CHECK_INFO;
        this.actOptions = new MenuText[baseInfo.ACT_OPTIONS.length];
        for (int i = 0; i < baseInfo.ACT_OPTIONS.length; ++i)
        {
            String[] actualActOptions = new String[baseInfo.ACT_OPTIONS[i].length + 1];
            actualActOptions[0] = "* Check";
            for (int j = 0; j < baseInfo.ACT_OPTIONS[i].length; ++j)
            {
                actualActOptions[j + 1] = "* " + baseInfo.ACT_OPTIONS[i][j];
            }
            this.actOptions[i] = new MenuText(actualActOptions);
        }

        this.stateOptions = baseInfo.STATE_OPTIONS.clone();
        this.minSparableState = baseInfo.SPAREABLE_STATE;

        this.actTexts = baseInfo.ACT_TEXTS.clone();

        this.stateChanges = baseInfo.STATE_CHANGES.clone();

        this.stateTexts = baseInfo.STATE_TEXTS.clone();

        this.state = 0;

        this.monster = m;
    }

    public ActInformation(AbstractMonster m)
    {
        this.checkInfo = GenerateCheckInfo(m);
        this.actOptions = new MenuText[1];
        this.actOptions[0] = new MenuText(new String[] {"* Check"});

        this.stateOptions = new int[1];
        this.stateOptions[0] = 0; //apparently this is the default value, whatever
        this.minSparableState = 1; //Impossible to spare, other than by weakening.

        this.actTexts = null;
        this.stateChanges = null;
        this.stateTexts = GenerateStateTexts(m);

        this.state = 0;

        this.monster = m;
    }

    public void DoAct(int actIndex)
    {
        AbstractDungeon.actionManager.addToBottom(new TextBoxAction(new UndertaleText(actTexts[state][actIndex])));
        AbstractDungeon.actionManager.addToBottom(new WaitForTextDoneAction());

        this.state = stateChanges[state][actIndex];

        if (this.state >= minSparableState)
        {
            SpareableEnemies.Spareable.set(monster, true);
        }
    }
}
