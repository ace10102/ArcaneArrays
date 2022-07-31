package com.spoilers.arcanearrays.arrays.functions;

import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;

import net.minecraft.resources.ResourceLocation;

public class NoExplosionFunction extends ArrayFunction {
    
    private static final ResourceLocation component = new ResourceLocation("mna","components/explosion");

    public NoExplosionFunction(ResourceLocation registryName) {
        super(registryName, component, false);
    }

    @Override
    public boolean applyFunction(SpellTarget target, ArrayDefinition array) {
        return false;
    }
}