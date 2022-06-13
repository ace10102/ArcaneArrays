package com.spoilers.arcanearrays.blocks;

import com.spoilers.arcanearrays.ArcaneArrays;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AABlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArcaneArrays.MOD_ID);
    /* Blocks */
        // Test
    public static final RegistryObject<Block> TESTCORE = BLOCKS.register("test_core", () -> new TestCore());
        // Building
    //public static final RegistryObject<Block> VINTEUM_POWDER = BLOCKS.register("vinteum_powder", () -> new FallingBlock(Block.Properties.copy(Blocks.SAND)));
}
