package UnderSpire.Attacks.Patterns;

import UnderSpire.Abstracts.Undertale.Attacks.AttackPattern;
import UnderSpire.Attacks.AttackObjects.LouseTackle;
import UnderSpire.UI.BattleArea;
import UnderSpire.UI.MinigameSoul;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class LouseTackleAttack extends AttackPattern {
    private float spawnTimer;

    private ArrayList<LouseTackle> tackles;

    private static float MIN_SPAWN_DELAY = 0.3f;
    private static float SPAWN_DELAY = 0.7f;
    private static float SPAWN_REDUCE_SCALE = 0.02f;

    public LouseTackleAttack(DamageInfo info)
    {
        super(info, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        tackles = new ArrayList<>();

        spawnTimer = MathUtils.random(MIN_SPAWN_DELAY, SPAWN_DELAY - (damageInfo.base * SPAWN_REDUCE_SCALE));
    }

    private void spawn(MinigameSoul p, BattleArea area)
    {
        tackles.add(new LouseTackle(p, area, damageInfo.base));
    }

    @Override
    public void render(float deltaTime, SpriteBatch sb, MinigameSoul p, BattleArea area) {
        spawnTimer -= deltaTime;
        if (spawnTimer <= 0.0f)
        {
            spawn(p, area);
            spawnTimer = Math.max(MIN_SPAWN_DELAY, Math.max(SPAWN_DELAY - (damageInfo.base * SPAWN_REDUCE_SCALE), MIN_SPAWN_DELAY));
        }

        ArrayList<LouseTackle> toRemove = new ArrayList<>();

        for (LouseTackle t : tackles)
        {
            t.render(deltaTime, sb, p, area);
            if (t.isDone)
            {
                toRemove.add(t);
            }
        }

        tackles.removeAll(toRemove);
    }

    @Override
    public boolean checkHit(MinigameSoul player) {
        boolean hit = false;
        for (LouseTackle t : tackles)
        {
            if (t.checkHit(player))
                hit = true;
        }
        return hit;
    }

    @Override
    public void dispose() {
        for (LouseTackle t : tackles)
        {
            t.dispose();
        }
    }
}
