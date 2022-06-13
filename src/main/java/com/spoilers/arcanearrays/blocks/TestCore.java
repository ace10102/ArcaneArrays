package com.spoilers.arcanearrays.blocks;

import java.util.Random;

import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.base.ISpellDefinition;
import com.spoilers.arcanearrays.capabilities.world.ArrayCapProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TestCore extends Block implements ISpellInteractibleBlock<TestCore> {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    protected static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public TestCore() {
        super(BlockBehaviour.Properties.of(Material.GRASS).noOcclusion().strength(1.0f));
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, Boolean.valueOf(false)));
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        if (!worldIn.isClientSide) {
            BlockState newstate = state;
            newstate = state.setValue(ACTIVE, Boolean.valueOf(state.getValue(ACTIVE) == false));
            if (newstate.getValue(ACTIVE).booleanValue()) {
                if (player.getItemInHand(handIn).getItem() != Items.FLINT_AND_STEEL) {
                    return InteractionResult.FAIL;
                }
                worldIn.getCapability(ArrayCapProvider.ARRAYS).ifPresent(w -> w.addArrayLocation(pos));
                worldIn.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            } else {
                worldIn.getCapability(ArrayCapProvider.ARRAYS).ifPresent(w -> w.removeArrayLocation(pos));
                worldIn.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            worldIn.setBlock(pos, newstate, 1);
            worldIn.sendBlockUpdated(pos, state, newstate, 2);
        }
        return InteractionResult.SUCCESS;
    }

    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(ACTIVE).booleanValue()) {
            worldIn.addParticle(ParticleInit.BLUE_FLAME.get(), (pos.getX() + 0.5f), (pos.getY() + 1), (pos.getZ() + 0.5f), 0.0, 0.01f, 0.0);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{ACTIVE});
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(ACTIVE) != false ? 15 : 5;
    }

    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack) {
        if (!worldIn.isClientSide) {
            worldIn.getCapability(ArrayCapProvider.ARRAYS).ifPresent(w -> w.removeArrayLocation(pos));
        }
        super.playerDestroy(worldIn, player, pos, state, te, stack);
    }

    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isClientSide) {
            worldIn.getCapability(ArrayCapProvider.ARRAYS).ifPresent(w -> w.removeArrayLocation(pos));
        }
    }

    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide) {
            worldIn.getCapability(ArrayCapProvider.ARRAYS).ifPresent(w -> w.removeArrayLocation(pos));
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public boolean onHitBySpell(Level world, BlockPos pos, ISpellDefinition spell) {
        BlockState existing;
        if (!world.isClientSide && spell.getAffinity().containsKey(Affinity.FIRE) && (existing = world.getBlockState(pos)).hasProperty(ACTIVE) && !(existing.getValue(ACTIVE)).booleanValue()) {
            BlockState newState = existing.setValue(ACTIVE, Boolean.valueOf(true));
            world.getCapability(ArrayCapProvider.ARRAYS).ifPresent(w -> w.addArrayLocation(pos));
            world.setBlock(pos, newState, 1);
            world.sendBlockUpdated(pos, existing, newState, 2);
            return true;
        }
        return false;
    }

    public static boolean shouldEntityBeBlocked(Entity entity) {
        if (!entity.canChangeDimensions()) {
            return false;
        }
        return entity instanceof Enemy || entity instanceof Chicken && ((Chicken)entity).isChickenJockey();
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }
}
