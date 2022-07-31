package com.spoilers.arcanearrays.arrays.arrangements;

import java.util.List;

import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class UtilityArrangement extends ArrayArrangement {
    
    private static final ResourceLocation tagLoc = new ResourceLocation("arcanearrays:utility_arrangement");

    public UtilityArrangement(ResourceLocation registryName) {
        super(registryName, tagLoc);
    }

    @Override
    public List<SpellTarget> CollectFilteredTargets(SpellSource source, Level level, ArrayDefinition array) {
        return null;
    }
    
}
