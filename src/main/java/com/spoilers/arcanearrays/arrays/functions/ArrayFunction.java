package com.spoilers.arcanearrays.arrays.functions;

import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;
import com.spoilers.arcanearrays.arrays.ArrayModuleRegistry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ArrayFunction extends ForgeRegistryEntry<ArrayFunction> {
    
    private final ResourceLocation component;
    private final boolean hasAdditional;
    
    public ArrayFunction(ResourceLocation registryName, ResourceLocation pComponent, boolean pEffect) {
        this.setRegistryName(registryName);
        this.component = pComponent;
        this.hasAdditional = pEffect;
    }
    
    public abstract boolean applyFunction(SpellTarget target, ArrayDefinition array);
    
    public ResourceLocation getComponent() {
        return this.component;
    }
    
    public boolean hasAdditional() {
        return this.hasAdditional;
    }
    
    public CompoundTag toNBT() {
        CompoundTag function = new CompoundTag();
        function.putString("function_name", this.getRegistryName().toString());
        return function;
    }
    
    public static <F extends ArrayFunction> F fromNBT(CompoundTag nbt) {
        F function = (F)ArrayModuleRegistry.Functions.get().getValue(new ResourceLocation(nbt.getString("function_name")));
        if (function.hasAdditional()) {
            function.loadAdditional(nbt);
        }
        return function;
    }
    public void loadAdditional(CompoundTag nbt) {
        
    }
    
    public static class PhantomFunction extends ArrayFunction {

        public static PhantomFunction instance = new PhantomFunction();
        private static final ResourceLocation _default = new ResourceLocation("arcanearrays:function_default");

        public PhantomFunction() {
            super(_default, _default, false);
        }

        @Override
        public boolean applyFunction(SpellTarget target, ArrayDefinition array) {
            return true;
        }
    }
}