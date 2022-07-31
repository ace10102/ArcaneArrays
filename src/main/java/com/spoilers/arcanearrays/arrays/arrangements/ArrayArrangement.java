package com.spoilers.arcanearrays.arrays.arrangements;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;
import com.spoilers.arcanearrays.arrays.IModularArrayPart;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.tags.ITag;

public abstract class ArrayArrangement extends ForgeRegistryEntry<ArrayArrangement> implements IModularArrayPart {
    
    //private Integer tier = null;
    //private final ImmutableList<AttributeValuePair> modifiableAttributes;
    private ResourceLocation recipeTag;

    public ArrayArrangement(ResourceLocation registryName, ResourceLocation tagLoc) {
        this.setRegistryName(registryName);
        this.recipeTag = tagLoc;
        //this.modifiableAttributes = ImmutableList.copyOf(attributeValuePairs);
    }
    
    public List<SpellTarget> CollectPotentialEntityTargets(SpellSource source, Level level, ArrayDefinition array) {
        AABB aoe = array.aoe;
        return level.getEntitiesOfClass(LivingEntity.class, aoe).stream().map(e -> new SpellTarget(e)).collect(Collectors.toList());
    }
    
    public ITag<Item> getArrangementRecipeTag() {
        TagKey<Item> key = ForgeRegistries.ITEMS.tags().createTagKey(recipeTag);
        return ForgeRegistries.ITEMS.tags().isKnownTagName(key) ? ForgeRegistries.ITEMS.tags().getTag(key) : null;
    }
    
    public abstract List<SpellTarget> CollectFilteredTargets(SpellSource source, Level level, ArrayDefinition array);

    public static class PhantomArrangement extends ArrayArrangement {
        
        public static PhantomArrangement instance = new PhantomArrangement();
        private static final ResourceLocation _default = new ResourceLocation("arcanearrays:arrangement_default");

        public PhantomArrangement() {
            super(_default, _default);
        }

        @Override
        public List<SpellTarget> CollectFilteredTargets(SpellSource source, Level world, ArrayDefinition array) {
            return Arrays.asList(SpellTarget.NONE);
        }
    }
}