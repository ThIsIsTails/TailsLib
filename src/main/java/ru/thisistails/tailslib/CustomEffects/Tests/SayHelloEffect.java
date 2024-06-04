package ru.thisistails.tailslib.CustomEffects.Tests;

import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomEffects.CustomEffect;
import ru.thisistails.tailslib.CustomEffects.EffectInterruptionReason;
import ru.thisistails.tailslib.CustomEffects.Data.AppliedEffectData;
import ru.thisistails.tailslib.CustomEffects.Data.CustomEffectData;

public class SayHelloEffect implements CustomEffect {

    @Override
    public @NotNull CustomEffectData getEffectData() {
        return new CustomEffectData("sayhelloeffect", "Say hello effect!", 0, false, true);
    }

    @Override
    public void onEffectEnd(AppliedEffectData data) {
        data.getPlayer().sendMessage("Goodbye!");
    }

    @Override
    public void onEffectInterrupted(AppliedEffectData data, EffectInterruptionReason reason) {
        data.getPlayer().sendMessage("Effect has been interrupted because of " + reason.toString());
    }

    @Override
    public void onTickCall(AppliedEffectData data) {
        data.getPlayer().sendMessage(String.valueOf(data.getDuration()));
    }
}
