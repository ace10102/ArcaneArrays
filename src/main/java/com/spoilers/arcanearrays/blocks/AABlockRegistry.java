package com.spoilers.arcanearrays.blocks;

import com.spoilers.arcanearrays.ArcaneArrays;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AABlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArcaneArrays.MOD_ID);
    /* Array Cores */
    public static final RegistryObject<Block> ARRAY_CORE = BLOCKS.register("array_core", () -> new ArrayCore());
    public static final RegistryObject<Block> ARRAY_FOCUS = BLOCKS.register("array_focus", () -> new ArrayFocus());

}