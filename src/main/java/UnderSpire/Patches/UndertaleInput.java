package UnderSpire.Patches;

import UnderSpire.Interfaces.UndertalePlayer;
import com.badlogic.gdx.Input;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;

public class UndertaleInput {
    public static class InputActions
    {
        public static InputAction undertaleConfirm = null;
        public static InputAction undertaleBack = null;
    }

    @SpirePatch(
            clz = InputActionSet.class,
            method = "load"
    )
    public static class LoadPatch
    {
        @SpirePrefixPatch
        public static void LoadUndertaleInput()
        {
            InputActions.undertaleConfirm = new InputAction(InputActionSet.prefs.getInteger("UNDERTALE_CONFIRM", Input.Keys.Z));
            InputActions.undertaleBack = new InputAction(InputActionSet.prefs.getInteger("UNDERTALE_BACK", Input.Keys.X));
        }
    }

    @SpirePatch(
            clz = InputAction.class,
            method = "isJustPressed"
    )
    @SpirePatch(
            clz = InputAction.class,
            method = "isPressed"
    )
    public static class PreventConflictsPress
    {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(InputAction __instance)
        {
            if (__instance.equals(InputActions.undertaleConfirm) || __instance.equals(InputActions.undertaleBack))
            {
                return SpireReturn.Continue();
            }
            else if (AbstractDungeon.player instanceof UndertalePlayer &&
                    (__instance.getKey() == InputActions.undertaleConfirm.getKey() ||
                            __instance.getKey() == InputActions.undertaleBack.getKey())) {
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }

    /* For now, not saving.
    @SpirePatch(
            clz = InputActionSet.class,
            method = "save"
    )
    public static class SavePatch
    {
        @SpirePrefixPatch
        public static void SaveUndertaleInput()
        {
            InputActionSet.prefs.putInteger("UNDERTALE_CONFIRM", InputActions.undertaleConfirm.get().getKey());
            InputActionSet.prefs.putInteger("UNDERTALE_BACK", InputActions.undertaleBack.get().getKey());
        }
    }
    */
}
