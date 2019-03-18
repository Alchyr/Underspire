package UnderSpire.UI;

import UnderSpire.Actions.General.EndTurnNowAction;
import UnderSpire.Actions.Undertale.TextBoxAction;
import UnderSpire.Actions.Undertale.WaitForTextDoneAction;
import UnderSpire.Interfaces.UndertalePlayer;
import UnderSpire.Patches.SpareableEnemies;
import UnderSpire.UI.SubUI.MainButtons;
import UnderSpire.UI.SubUI.TextBox;
import UnderSpire.Util.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.JackOfAllTrades;
import com.megacrit.cardcrawl.cards.red.SwordBoomerang;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.Pantograph;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import java.util.ArrayList;

import static UnderSpire.UnderSpire.battleUI;
import static UnderSpire.UnderSpire.logger;
import static UnderSpire.Util.Sounds.MENU_SELECT;

public class BattleUI {
    private TextBox textDisplay;
    private MainButtons buttons;

    private UndertaleText currentTurnText;

    private AbstractMonster actTarget;

    private MenuState state;

    public enum MenuState
    {
        MAIN,
        FIGHT,
        ATTACKING, //Trying to hit that perfect hit
        ACT,
        ACT_OPTIONS,
        ITEMS,
        MERCY
    }


    public BattleUI()
    {
        textDisplay = new TextBox();
        buttons = new MainButtons(textDisplay);

        state = MenuState.MAIN;
    }

    public void update(float deltaTime)
    {
        textDisplay.update(deltaTime);
        buttons.update(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        textDisplay.render(sb);
        buttons.render(sb);
    }

    public void menuPressed(int index)
    {
        CardCrawlGame.sound.play(MENU_SELECT.getKey());
        switch (state)
        {
            case FIGHT:
                AbstractMonster fightTarget = UndertaleStringHelper.getSelectedMonster(index);

                if (fightTarget != null && AbstractDungeon.player instanceof UndertalePlayer)
                {
                    UndertalePlayer p = (UndertalePlayer)AbstractDungeon.player;
                    WeaponHits hits = p.getWeaponHits();
                    hits.target = fightTarget;
                    startAttack(hits);
                }
                break;
            case ACT:
                actTarget = UndertaleStringHelper.getSelectedMonster(index);

                if (actTarget != null)
                {
                    if (!ActInformation.activeActInformation.containsKey(actTarget))
                    {
                        ActInformation.GenerateActInfo(actTarget);
                    }

                    battleUI.setMenuText(UndertaleStringHelper.getEnemyActOptions(actTarget));
                    battleUI.setMenuState(BattleUI.MenuState.ACT_OPTIONS);
                }
                break;
            case ACT_OPTIONS:
                if (actTarget != null && ActInformation.activeActInformation.containsKey(actTarget))
                {
                    textDisplay.clearText();

                    if (index == 0) //Check
                    {
                        buttons.enabled = false;
                        buttons.active = false;
                        AbstractDungeon.actionManager.addToBottom(new TextBoxAction(UndertaleStringHelper.getCheckInfo(actTarget)));
                        AbstractDungeon.actionManager.addToBottom(new WaitForTextDoneAction());
                        AbstractDungeon.actionManager.addToBottom(new EndTurnNowAction());
                    }
                    else
                    {
                        buttons.enabled = false;
                        buttons.active = false;
                        ActInformation.activeActInformation.get(actTarget).DoAct(index - 1);
                        AbstractDungeon.actionManager.addToBottom(new EndTurnNowAction());
                    }
                }
                break;
            case ITEMS:
                AbstractCard item = UndertaleStringHelper.getSelectedItem(index);
                if (item != null)
                {
                    textDisplay.clearText();

                    item.use(AbstractDungeon.player, null);
                    if (FleetingField.fleeting.get(item))
                    {
                        AbstractCard removeCard = null;
                        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
                        {
                            if (c.uuid.equals(item.uuid))
                            {
                                removeCard = c;
                            }
                        }
                        AbstractDungeon.player.masterDeck.removeCard(removeCard);
                    }

                    buttons.enabled = false;
                    buttons.active = false;
                    AbstractDungeon.actionManager.addToBottom(new EndTurnNowAction());
                }
                break;
            case MERCY:
                switch (index)
                {
                    case 0: //SPARE
                        ArrayList<AbstractMonster> sparedEnemies = new ArrayList<>();
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                        {
                            if (!m.isDeadOrEscaped() && SpareableEnemies.Spareable.get(m))
                            {
                                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
                                sparedEnemies.add(m);
                            }
                        }

                        textDisplay.clearText();
                        buttons.enabled = false;
                        buttons.active = false;
                        AbstractDungeon.actionManager.addToBottom(new EndTurnNowAction());
                        break;
                    case 1: //FLEE

                        break;
                }
                break;
        }
    }

    public float getHitRightPosition()
    {
        return textDisplay.rightX;
    }
    public float getHitLeftPosition()
    {
        return textDisplay.leftX;
    }
    public float getCenterX()
    {
        return textDisplay.cX;
    }
    public float getCenterY()
    {
        return textDisplay.cY;
    }

    public boolean textIsDone()
    {
        return textDisplay.isDone();
    }

    public void turnStartText()
    {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            if (GameActionManager.turn == 0)
            {
                String monsterGroup = "";
                boolean boss = false;

                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                {
                    if (m.type == AbstractMonster.EnemyType.BOSS)
                        boss = true;
                }
                if (boss)
                {
                    monsterGroup = AbstractDungeon.bossList.get(0);
                }
                else if (AbstractDungeon.getCurrRoom().eliteTrigger)
                {
                    monsterGroup = AbstractDungeon.eliteMonsterList.get(0);
                }
                else
                {
                    monsterGroup = AbstractDungeon.monsterList.get(0);
                }
                logger.info("Expected MonsterGroup: " + monsterGroup);

                UndertaleText firstTurnText = UndertaleStringHelper.getEnemyFirstTurnText(monsterGroup);

                if (firstTurnText != null)
                {
                    currentTurnText = firstTurnText;
                    textDisplay.setText(currentTurnText);
                }
                else
                {
                    currentTurnText = UndertaleStringHelper.getEnemyText(AbstractDungeon.getRandomMonster());
                    textDisplay.setText(currentTurnText);
                }
                buttons.active = true;
                buttons.enabled = true;
                state = MenuState.MAIN;
            }
            else
            {
                currentTurnText = UndertaleStringHelper.getEnemyText(AbstractDungeon.getRandomMonster());
                textDisplay.setText(currentTurnText);
                buttons.active = true;
                buttons.enabled = true;
                state = MenuState.MAIN;
            }
        }
    }
    public void setText(UndertaleText text)
    {
        textDisplay.setText(text);
    }
    public void setMenuText(MenuText text)
    {
        textDisplay.setMenuText(text);
        buttons.active = false;
    }
    public void setMessageText(UndertaleText text)
    {
        textDisplay.setActiveText(text);
        buttons.active = false;
        buttons.enabled = false;
    }
    public void setMenuState(MenuState state)
    {
        this.state = state;
    }

    public void startAttack(WeaponHits hits)
    {
        setMenuState(MenuState.ATTACKING);
        textDisplay.startAttack(hits);
        buttons.active = false;
        buttons.enabled = false;
    }

    public void back()
    {
        switch (state)
        {
            case ACT_OPTIONS:
                battleUI.setMenuText(UndertaleStringHelper.getEnemyOptionsText());
                state = MenuState.ACT;
                break;
            default:
                currentTurnText.reset();
                textDisplay.setText(currentTurnText);
                buttons.active = true;
                buttons.enabled = true;
                state = MenuState.MAIN;
        }
    }
}
