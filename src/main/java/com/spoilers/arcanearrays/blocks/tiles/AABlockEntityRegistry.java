package com.spoilers.arcanearrays.blocks.tiles;

import com.spoilers.arcanearrays.ArcaneArrays;
import com.spoilers.arcanearrays.blocks.AABlockRegistry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AABlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ArcaneArrays.MOD_ID);
    public static final RegistryObject<BlockEntityType<ArrayTile>> ARRAY_TE = TILE_ENTITIES.register("array_tile_entity", () -> BlockEntityType.Builder.of(ArrayTile::new, new Block[]{AABlockRegistry.ARRAY_CORE.get()}).build(null));

}
