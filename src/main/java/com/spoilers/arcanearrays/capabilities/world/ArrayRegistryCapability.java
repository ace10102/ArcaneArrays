package com.spoilers.arcanearrays.capabilities.world;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Optional;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;


public class ArrayRegistryCapability {
    
    private HashMap<BlockPos, ArrayDefinition> ARRAY_REGISTRY = new HashMap<BlockPos, ArrayDefinition>();
    private final String KEY_ARRAY_POS = "position_key";
    private final String VAl_ARRAY_DATA = "array_data";

    public HashMap<BlockPos, ArrayDefinition> getNearbyArrays(BlockPos origin, int max) {
        int maxSq = max * max;
        return this.ARRAY_REGISTRY.entrySet().stream().filter(e -> {
            double dist = e.getKey().distSqr(origin);
            return dist <= maxSq;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new));
    }
    
    public boolean isEmpty() {
        return ARRAY_REGISTRY.isEmpty();
    }
    
    public Optional<ArrayDefinition> getArrayAt(BlockPos pos) {
        return Optional.fromNullable(this.ARRAY_REGISTRY.getOrDefault(pos, null));
    }
    
    public boolean addArray(BlockPos origin, ArrayDefinition arrayData) {
        this.ARRAY_REGISTRY.put(origin, arrayData);
        return true;
    }
    public boolean removeArray(BlockPos origin, ArrayDefinition arrayData) {
        return this.ARRAY_REGISTRY.remove(origin, arrayData);
    }
    
    public CompoundTag writeToNBT(CompoundTag nbt) {
        ListTag arrayData = new ListTag();
        this.ARRAY_REGISTRY.entrySet().stream().forEach(e -> {
            CompoundTag array = new CompoundTag();
            array.putLong(KEY_ARRAY_POS, e.getKey().asLong());
            e.getValue().writeToNBT(nbt);
            arrayData.add(array);
        });
        nbt.put(VAl_ARRAY_DATA, arrayData);
        return nbt;
    }
    
    public void readFromNBT(CompoundTag nbt) {
        if (nbt.contains(VAl_ARRAY_DATA)) {
            ListTag arrayData = nbt.getList(VAl_ARRAY_DATA, 10);
            this.ARRAY_REGISTRY.clear();
            arrayData.forEach(inbt -> {
                CompoundTag array = (CompoundTag)inbt;
                BlockPos pos = BlockPos.of(array.getLong(KEY_ARRAY_POS));
                ArrayDefinition value = ArrayDefinition.readFromNBT(array);
                this.ARRAY_REGISTRY.put(pos, value);
            });
        }
    }
}
