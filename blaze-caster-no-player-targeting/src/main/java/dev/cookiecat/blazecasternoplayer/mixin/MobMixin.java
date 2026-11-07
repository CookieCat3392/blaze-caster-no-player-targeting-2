package dev.cookiecat.blazecasternoplayer.mixin;

import dev.cookiecat.blazecasternoplayer.BlazeCasterNoPlayerTargeting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stops a mob that has been marked as a Blaze Caster summon from acquiring
 * any player as its target, including through its own autonomous AI goals.
 */
@Mixin(Mob.class)
public abstract class MobMixin {
    @Inject(
        method = "setTarget(Lnet/minecraft/world/entity/LivingEntity;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void bcnpt$preventCasterSummonsTargetingPlayers(
        LivingEntity newTarget,
        CallbackInfo ci
    ) {
        Mob self = (Mob) (Object) this;

        if (newTarget instanceof Player
            && self.getTags().contains(BlazeCasterNoPlayerTargeting.SUMMON_TAG)) {
            ci.cancel();
        }
    }
}
