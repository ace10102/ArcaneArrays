package com.spoilers.arcanearrays.arrays.functions;

import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;

import net.minecraft.resources.ResourceLocation;

public class BarrierFunction extends ArrayFunction {
    
    private static final ResourceLocation component = new ResourceLocation("mna","shapes/bound_shield");
    
    public BarrierFunction(ResourceLocation registryName) {
        super(registryName, component, false);
        
    }

    @Override
    public boolean applyFunction(SpellTarget target, ArrayDefinition array) {
        // TODO Auto-generated method stub
        return false;
    }
}