package com.spoilers.arcanearrays.arrays.arrangements;

import java.util.List;
import java.util.stream.Collectors;

import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

public class DefensiveArrangement extends ArrayArrangement {
    
    private static final ResourceLocation tagLoc = new ResourceLocation("arcanearrays:defensive_arrangement");
    
    public DefensiveArrangement(ResourceLocation registryName) {
        super(registryName, tagLoc);
    }

    @Override
    public List<SpellTarget> CollectFilteredTargets(SpellSource source, Level level, ArrayDefinition array) {
        return level.getEntitiesOfClass(LivingEntity.class, array.getArea(), entity -> entity == source.getCaster() || entity.isAlliedTo(source.getCaster()) || entity.getType().getCategory() == MobCategory.CREATURE || entity.getType().getCategory() == MobCategory.WATER_CREATURE).stream().map(e -> new SpellTarget(e)).collect(Collectors.toList());
    }
}