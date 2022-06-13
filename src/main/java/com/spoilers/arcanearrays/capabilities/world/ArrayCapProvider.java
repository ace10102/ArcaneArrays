package com.spoilers.arcanearrays.capabilities.world;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.spoilers.arcanearrays.ArcaneArrays;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ArrayCapProvider implements ICapabilitySerializable<Tag> {
    private boolean isOverworld = false;
    public static final Capability<ArrayCap> ARRAYS = CapabilityManager.get(new CapabilityToken<ArrayCap>(){});
    private final LazyOptional<ArrayCap> holder = LazyOptional.of(() -> new ArrayCap(this.isOverworld));
    private final String KEY_ARRAY_LOCATIONS = "array_locations";

    public ArrayCapProvider(boolean isOverworld) {
        this.isOverworld = isOverworld;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return ARRAYS.orEmpty(cap, this.holder);
    }

    public Tag serializeNBT() {
        ArrayCap instance = this.holder.orElse(null);
        CompoundTag nbt_main = new CompoundTag();
        nbt_main.put(KEY_ARRAY_LOCATIONS, new LongArrayTag(instance.getAllArrayLocations()));
        return nbt_main;
    }

    public void deserializeNBT(Tag nbt) {
        ArrayCap instance = this.holder.orElse(null);
        if (!(nbt instanceof CompoundTag)) {
            ArcaneArrays.LOGGER.error("Array NBT passed back not an instance of CompoundNBT - all save data was NOT loaded!");
            return;
        }
        CompoundTag nbt_main = (CompoundTag)nbt;
        if (nbt_main.contains(KEY_ARRAY_LOCATIONS, 12)) {
            instance.setArrayLocations(Arrays.stream(nbt_main.getLongArray(KEY_ARRAY_LOCATIONS)).boxed().collect(Collectors.toList()));
        }
    }
}


