package com.spoilers.arcanearrays.blocks;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.spells.base.ISpellDefinition;
import com.spoilers.arcanearrays.blocks.tiles.ArrayTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArrayFocus extends Block implements ISpellInteractibleBlock<ArrayFocus> {
    
    protected static final VoxelShape SHAPE = Block.box(6.0, 7.0, 6.0, 10.0, 16.0, 10.0);
    
    public ArrayFocus() {
        super(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().strength(1.0f));
    }
    
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean onHitBySpell(Level level, BlockPos pos, ISpellDefinition spell) {
        if(!level.isClientSide) {
            List<BlockPos> arrayPos = List.of(pos.north(3), pos.south(3), pos.east(3), pos.west(3));
            MutableBoolean returns = new MutableBoolean(false);
            arrayPos.forEach(aPos -> {
                BlockEntity te = level.getBlockEntity(aPos);
                if (te != null && te instanceof ArrayTile) {
                    ((ArrayTile)te).addFunction(level, pos, spell);
                    returns.setTrue();
                }
            });
            return returns.booleanValue();
        }
        return false;
    }
}