
package dev.cookiecat.blazecasternoplayer.mixin;

import dev.cookiecat.blazecasternoplayer.BlazeCasterNoPlayerTargeting;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.ttzplayz.create_wizardry.block.blaze_caster.BlazeCasterBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlazeCasterBlockEntity.class, remap = false)
public abstract class BlazeCasterBlockEntityMixin extends SmartBlockEntity {

    // Dummy constructor required for Java compilation.
    // Mixin replaces this class structure when Minecraft launches.
    protected BlazeCasterBlockEntityMixin(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState state
    ) {
        super(type, pos, state);
    }

    @Shadow
    @Final
    protected Set<UUID> trackedSummons;

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

                if (mob.getTarget() instanceof Player) {
                    mob.setTarget(null);
                }
            }
        }
    }
}
