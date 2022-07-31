package com.spoilers.arcanearrays.handlers;

import com.spoilers.arcanearrays.capabilities.world.ArrayRegistryCapability;
import com.spoilers.arcanearrays.arrays.functions.NoBreakingFunction;
import com.spoilers.arcanearrays.arrays.functions.NoExplosionFunction;
import com.spoilers.arcanearrays.capabilities.world.ArrayCapProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ArrayExecutionHandlers {
    
    public static final ResourceLocation ARRAY_CAP = new ResourceLocation("arcanearrays", "array_capability");
    
    @SubscribeEvent
    public void attachArraysCapability(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(ARRAY_CAP, new ArrayCapProvider());
    }
    
    @SubscribeEvent
    public void selectAndFilterBlock(RightClickBlock event) {
//        if(event.getWorld().isClientSide())
//            return;
//        System.out.println("space");
//        Level world = event.getWorld();
//        ArrayRegistryCapability arrays = world.getCapability(ArrayCapProvider.ARRAYS).orElse(null);
//        if (world.isClientSide || arrays == null || arrays.isEmpty()) return;
        //System.out.println(arrays.getNearbyArrays(event.getPos(), 20));
        
        //arrays.getNearbyArrays(event.getPos(), 64).forEach((pos, array) -> System.out.println(array.aoe.intersects(new AABB(event.getPos()))));
    }
    
    @SubscribeEvent
    public void preventExplosions(ExplosionEvent.Start event) {
        Level world = event.getWorld();
        ArrayRegistryCapability arrays = world.getCapability(ArrayCapProvider.ARRAYS).orElse(null);
        if (world.isClientSide || arrays == null || arrays.isEmpty()) return;
        arrays.getNearbyArrays(new BlockPos(event.getExplosion().getPosition()), 64).forEach((pos, array) -> {
            if (array.getArea().contains(event.getExplosion().getPosition())) {
                array.getFunctions().forEach(function -> {
                    if (function instanceof NoExplosionFunction) {
                        event.setCanceled(true);
                        return;
                    }
                });
            }
        });
    }
    
    @SubscribeEvent
    public void preventBreaking(BreakEvent event) {
        Level level = event.getPlayer().getLevel();
        ArrayRegistryCapability arrays = level.getCapability(ArrayCapProvider.ARRAYS).orElse(null);
        if (level.isClientSide || arrays == null || arrays.isEmpty()) return;
        arrays.getNearbyArrays(event.getPos(), 64).forEach((pos, array) -> {
            if (array.getArea().contains(Vec3.atCenterOf(event.getPos()))) {
                array.getFunctions().forEach(function -> {
                    if (function instanceof NoBreakingFunction && !event.getPlayer().getUUID().equals(array.getCaster())) {
                        event.setCanceled(true);
                        return;
                    }
                });
            }
        });
    }
}