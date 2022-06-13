package com.spoilers.arcanearrays.capabilities.world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mna.capabilities.worlddata.WellspringNodeRegistry;

import net.minecraft.core.BlockPos;

public class ArrayCap {
    
    private ArrayList<BlockPos> array_list;
    
    public ArrayCap() {
        this(false);
    }

    public ArrayCap(boolean isOverworld) {
        this.array_list = new ArrayList();
    }
    
    public List<Long> getAllArrayLocations() {
        return this.array_list.stream().map(bp -> bp.asLong()).collect(Collectors.toList());
    }
    
    public void setArrayLocations(List<Long> locations) {
        this.array_list.clear();
        this.array_list.addAll(locations.stream().map(l -> BlockPos.of(l)).collect(Collectors.toList()));
    }
    
    public void addArrayLocation(BlockPos location) {
        long posLong = location.asLong();
        if (!this.array_list.stream().anyMatch(pos -> pos.asLong() == posLong)) {
            this.array_list.add(location);
        }
    }
    
    public void removeArrayLocation(BlockPos location) {
        long posLong = location.asLong();
        this.array_list.removeIf(pos -> pos.asLong() == posLong);
    }
    
    public BlockPos findClosestArray(BlockPos pos) {
        long candle = getAllArrayLocations().stream().map(e -> e).sorted(new Comparator<Long>() {

            @Override
            public int compare(Long o1, Long o2) {
                Double d1 = BlockPos.of(o1).distSqr(pos);
                Double d2 = BlockPos.of(o2).distSqr(pos);
                return d1.compareTo(d2);
            }
        }).findFirst().get();

        return BlockPos.of(candle);
    }
    
    public boolean isWithinArray(BlockPos pos) {
        return this.array_list.stream().anyMatch(p -> Math.abs(p.getX() - pos.getX()) <= 32 && Math.abs(p.getY() - pos.getY()) <= 32 && Math.abs(p.getZ() - pos.getZ()) <= 32);
    }
}
