package com.spoilers.arcanearrays.rituals;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.spoilers.arcanearrays.blocks.AABlocks;

import net.minecraft.resources.ResourceLocation;

public class RitualEffectTesting extends RitualEffect {

    public RitualEffectTesting(ResourceLocation ritualName) {
        super(ritualName);   
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        context.getWorld().setBlock(context.getCenter(), AABlocks.TESTCORE.get().defaultBlockState(), 0);
        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        
        return 0;
    }
}
