package dev.cookiecat.blazecasternoplayer.mixin;

import dev.cookiecat.blazecasternoplayer.BlazeCasterNoPlayerTargeting;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hooks Create: Wizardry's BlazeCasterBlockEntity.
 *
 * Create: Wizardry compiles this class itself, so its method and field names
 * are targeted without Minecraft name remapping.
 */
@Mixin(
    targets = "net.ttzplayz.create_wizardry.block.blaze_caster.BlazeCasterBlockEntity",
    remap = false
)
public abstract class BlazeCasterBlockEntityMixin {
    @Shadow
    @Final
    private Set<UUID> trackedSummons;

    /**
     * The Blaze Caster's findTarget() method uses this compiler-generated
     * predicate to decide which LivingEntities are valid targets.
     *
     * Returning false for every Player means the caster can still choose
     * hostile mobs and other non-player living entities, but never a player.
     */
    @Inject(
        method = "lambda$findTarget$2",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private void bcnpt$excludePlayersFromCasterTargets(
        LivingEntity candidate,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (candidate instanceof Player) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Create: Wizardry records newly spawned Mob entities in trackedSummons
     * after a spell is cast. Mark those exact entities with a persistent tag.
     *
     * This lets MobMixin distinguish Blaze Caster summons from normal mobs
     * and from summons created directly by a player.
     */
    @Inject(
        method = "trackNewEntities",
        at = @At("TAIL"),
        remap = false
    )
    private void bcnpt$markCasterSummons(
        ServerLevel serverLevel,
        Set<UUID> entitiesBeforeCast,
        LivingEntity castTarget,
        UUID placerUuid,
        CallbackInfo ci
    ) {
        for (UUID summonId : this.trackedSummons) {
            Entity entity = serverLevel.getEntity(summonId);
            if (entity instanceof Mob mob) {
                mob.addTag(BlazeCasterNoPlayerTargeting.SUMMON_TAG);

                // Clear a player target if one was assigned before the tag
                // was applied. Null targets are allowed by Mob#setTarget.
                if (mob.getTarget() instanceof Player) {
                    mob.setTarget(null);
                }
            }
        }
    }
}
