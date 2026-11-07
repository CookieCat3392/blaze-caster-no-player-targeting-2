package dev.cookiecat.blazecasternoplayer;

import net.neoforged.fml.common.Mod;

/**
 * Mod entry point. The behavior itself is implemented by the two mixins.
 */
@Mod(BlazeCasterNoPlayerTargeting.MOD_ID)
public final class BlazeCasterNoPlayerTargeting {
    public static final String MOD_ID = "blaze_caster_no_player_targeting";

    /**
     * Persistent entity tag applied only to mobs tracked as Blaze Caster summons.
     * MobMixin checks this tag before allowing a player to become the mob's target.
     */
    public static final String SUMMON_TAG = MOD_ID + ":caster_summon";

    public BlazeCasterNoPlayerTargeting() {
        // No event registration is required; the mixins implement the behavior.
    }
}
