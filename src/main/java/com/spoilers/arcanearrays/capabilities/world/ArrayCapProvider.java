package com.spoilers.arcanearrays.capabilities.world;

import com.spoilers.arcanearrays.ArcaneArrays;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ArrayCapProvider implements ICapabilitySerializable<Tag> {
    public static final Capability<ArrayRegistryCapability> ARRAYS = CapabilityManager.get(new CapabilityToken<ArrayRegistryCapability>(){});
    private final LazyOptional<ArrayRegistryCapability> holder = LazyOptional.of(() -> new ArrayRegistryCapability());
    private final String KEY_ARRAY_DATA = "array_data";

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return ARRAYS.orEmpty(cap, this.holder);
    }

    public Tag serializeNBT() {
        ArrayRegistryCapability instance = this.holder.orElse(null);
        CompoundTag nbt_main = new CompoundTag();
        //nbt_main.put(KEY_ARRAY_LOCATIONS, instance.writeToNBT(nbt_main));
        instance.writeToNBT(nbt_main);
        return nbt_main;
    }

    public void deserializeNBT(Tag nbt) {
        ArrayRegistryCapability instance = this.holder.orElse(null);
        if (!(nbt instanceof CompoundTag)) {
            ArcaneArrays.LOGGER.error("Array NBT passed back not an instance of CompoundNBT - all save data was NOT loaded!");
            return;
        }
        CompoundTag nbt_main = (CompoundTag)nbt;
        if (nbt_main.contains(KEY_ARRAY_DATA)) {
            instance.readFromNBT(nbt_main);
        }
    }
}


