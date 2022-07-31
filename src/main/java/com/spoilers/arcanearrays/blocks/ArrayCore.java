package com.spoilers.arcanearrays.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.base.ISpellDefinition;
import com.spoilers.arcanearrays.blocks.tiles.ArrayTile;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArrayCore extends Block implements EntityBlock, ISpellInteractibleBlock<ArrayCore> {
    
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    protected static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    private static final ResourceLocation manaweaving_wand = new ResourceLocation("mna:manaweaver_wand");
    private static final ResourceLocation manaweaving_wand_adv = new ResourceLocation("mna:manaweaver_wand_advanced");
    
    public ArrayCore() {
        super(BlockBehaviour.Properties.of(Material.GRASS).noOcclusion().strength(1.0f));
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, Boolean.valueOf(false)));
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArrayTile(pos, state);
    }
    
    public ArrayTile getThisBlockEntity(Level level, BlockPos pos) {
        return (level.getExistingBlockEntity(pos) != null || !(level.getExistingBlockEntity(pos) instanceof ArrayTile)) ? (ArrayTile)level.getExistingBlockEntity(pos) : null;
    }
    
    @Override
    public <T extends BlockEntity>BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return (lvl, pos, blockState, tile) -> {
            if (tile instanceof ArrayTile && state.getValue(ACTIVE).booleanValue())
                ((ArrayTile)tile).tick(lvl, pos, blockState, (ArrayTile)tile);
        };
    }
    
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity arrayTile = level.getBlockEntity(pos);
        if ((arrayTile != null && arrayTile instanceof ArrayTile) && (placer != null && placer instanceof Player)) {
            if (((ArrayTile)arrayTile).getOwner() == null) {
                ((ArrayTile)arrayTile).setOwner(((Player)placer).getUUID());
            }
        }
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult result) {
        if (!level.isClientSide) {
            BlockEntity arrayTile = level.getBlockEntity(pos);
            BlockState newState = state;
            newState = state.setValue(ACTIVE, Boolean.valueOf(state.getValue(ACTIVE) == false));
            if (arrayTile != null && arrayTile instanceof ArrayTile) {
                if (((ArrayTile)arrayTile).getOwner() == null || ((ArrayTile)arrayTile).getOwner() != player.getUUID()) {
                    ((ArrayTile)arrayTile).setOwner(player.getUUID());
                }
                if (newState.getValue(ACTIVE).booleanValue()) {
                    if (!player.getItemInHand(handIn).getItem().getRegistryName().equals(manaweaving_wand) && 
                            !player.getItemInHand(handIn).getItem().getRegistryName().equals(manaweaving_wand_adv)) {
                        return InteractionResult.FAIL;
                    }
                    ((ArrayTile)arrayTile).instateArray();
                    level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                } else {
                    ((ArrayTile)arrayTile).revokeArray();
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                level.setBlock(pos, newState, 1);
                level.sendBlockUpdated(pos, state, newState, 2);
            }
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
            this.getThisBlockEntity(worldIn, pos).revokeArray();
        }
        super.playerDestroy(worldIn, player, pos, state, te, stack);
    }

    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isClientSide) {
            this.getThisBlockEntity(worldIn, pos).revokeArray();
        }
    }

    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide) {
            this.getThisBlockEntity(worldIn, pos).revokeArray();
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public boolean onHitBySpell(Level world, BlockPos pos, ISpellDefinition spell) {
        BlockState existing;
        if (!world.isClientSide && spellContainsActivate(spell) && (existing = world.getBlockState(pos)).hasProperty(ACTIVE) && !(existing.getValue(ACTIVE)).booleanValue()) {
            BlockState newState = existing.setValue(ACTIVE, Boolean.valueOf(true));
            this.getThisBlockEntity(world, pos).instateArray();
            world.setBlock(pos, newState, 1);
            world.sendBlockUpdated(pos, existing, newState, 2);
            return true;
        }
        return false;
    }
    
    private boolean spellContainsActivate(ISpellDefinition spell) {
        ResourceLocation activate = new ResourceLocation("mna:components/activate");
        MutableBoolean returns = new MutableBoolean(false);
        spell.getComponents().forEach(component -> {
            if (component.getPart().getRegistryName().equals(activate))
                returns.setTrue();
        });
        return returns.getValue();
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }
}
