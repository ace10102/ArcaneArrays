package com.spoilers.arcanearrays.arrays.functions;

import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectApplyFunction extends ArrayFunction {
    
    private ResourceLocation potionEffect;

    public EffectApplyFunction(ResourceLocation registryName) {
        super(registryName, null, true);
    }
    
    public MobEffect getEffect() {
        return ForgeRegistries.MOB_EFFECTS.getValue(potionEffect);
    }
    public void setEffect(MobEffect effect) {
        this.potionEffect = effect.getRegistryName();
    }
    
    @Override
    public boolean applyFunction(SpellTarget target, ArrayDefinition array) {
        if (target.isLivingEntity()) {
            target.getLivingEntity().addEffect(new MobEffectInstance(this.getEffect(), 40));
        }
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