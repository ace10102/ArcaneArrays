package com.spoilers.arcanearrays.handlers;

import com.spoilers.arcanearrays.capabilities.world.ArrayCap;
import com.spoilers.arcanearrays.capabilities.world.ArrayCapProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TestHandler {
    @SubscribeEvent
    public void selectAndFilterBlock(RightClickBlock event) {
        Level world = event.getWorld();
        ArrayCap arrays = world.getCapability(ArrayCapProvider.ARRAYS).orElse(null);
        System.out.print(arrays);
        if (world.isClientSide || arrays == null) return;
        BlockPos array = arrays.findClosestArray(event.getPos());
        if (event.getPos().distSqr(array) < 15) {
            System.out.print(event.getPos().distSqr(array));
        }
    }
}
