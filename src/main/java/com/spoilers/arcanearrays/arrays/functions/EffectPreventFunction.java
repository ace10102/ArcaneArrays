package com.spoilers.arcanearrays.arrays.functions;

import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class EffectPreventFunction extends ArrayFunction {

    private ResourceLocation potionEffect;

    public EffectPreventFunction(ResourceLocation registryName) {
        super(registryName, null, true);
    }
    
    public void setEffect(MobEffect effect) {
        this.potionEffect = effect.getRegistryName();
    }
    
    @Override
    public boolean applyFunction(SpellTarget target, ArrayDefinition array) {
        
        return false;
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag function = new CompoundTag();
        function.putString("function_name", this.getRegistryName().toString());
        function.putString("effect", this.potionEffect.toString());
        return function;
    }
    
    @Override
    public void loadAdditional(CompoundTag nbt) {
        if (nbt.contains("effect"))
            this.potionEffect = new ResourceLocation(nbt.getString("effect"));
    }
}