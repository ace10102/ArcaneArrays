package com.spoilers.arcanearrays.blocks.tiles;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.spoilers.arcanearrays.arrays.ArrayDefinition;
import com.spoilers.arcanearrays.arrays.ArrayModuleRegistry;
import com.spoilers.arcanearrays.arrays.functions.ArrayFunction;
import com.spoilers.arcanearrays.arrays.functions.EffectApplyFunction;
import com.spoilers.arcanearrays.blocks.AABlockRegistry;
import com.spoilers.arcanearrays.blocks.ArrayCore;
import com.spoilers.arcanearrays.capabilities.world.ArrayCapProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ArrayTile extends BlockEntity {
    
    private ArrayDefinition array = new ArrayDefinition();
    private HashMap<BlockPos, ArrayFunction> functionCache = new HashMap<BlockPos, ArrayFunction>();
    private UUID owner;
    private SpellSource cachedSource = null;
    private List<SpellTarget> targetCache = null;
    private int arbitraryTickNumber = 0;
    
    public ArrayTile(BlockPos pos, BlockState state) {
        super(AABlockEntityRegistry.ARRAY_TE.get(), pos, state);
        this.array.setPos(pos);
    }
    
    public void tick(Level level, BlockPos pos, BlockState state, ArrayTile tile) {
        try {
        if (!level.isClientSide && state.getValue(ArrayCore.ACTIVE) && array != null) {
            arbitraryTickNumber ++;
            if (cachedSource == null)
                cachedSource = new SpellSource(level.getPlayerByUUID(owner), null,new Vec3(pos.getX(), pos.getY(), pos.getZ()), null);
            if (array.getArrangement()== null)
                array.setArrangement(ArrayModuleRegistry.Arrangements.get().getValue(new ResourceLocation("arcanearrays:arrangements/defensive")));
            if (array.getArrangement() != null && (targetCache == null || arbitraryTickNumber % 20 == 0))
                targetCache = array.getArrangement().CollectFilteredTargets(cachedSource, level, array);
            
            if (arbitraryTickNumber % 15 == 0 && targetCache != null && functionCache != null) {
                this.targetCache.forEach(target -> {
                    array.getFunctions().forEach(function -> {
                        function.applyFunction(target, array);
                    });
                });
                
                this.functionCache.forEach((bPos,func) -> {
                    if(!level.getBlockState(bPos).getBlock().equals(AABlockRegistry.ARRAY_FOCUS.get()))
                        this.removeFunction(func);
                });
            }
        }
        }
        catch(Exception ex) {
            System.out.println("array broke");
        }
    }
    
    public UUID getOwner() {
        return this.owner;
    }
    public void setOwner(UUID player) {
        this.owner = player;
    }
    
    public ArrayDefinition getArray() {
        return this.array;
    }
    
    public void addFunction(Level level, BlockPos pos, ISpellDefinition spell) {
        ResourceLocation effect = spell.getComponent(0).getPart().getRegistryName();
        ArrayModuleRegistry.Functions.get().forEach(function -> {
            if (function.getComponent() !=  null && function.getComponent().equals(effect)) {
                functionCache.put(pos, function);
                array.addFunction(function);
            }
        });
        MobEffect testEffect = this.testForEffect(level, pos, spell);
        if (testEffect != null) {
            EffectApplyFunction function = new EffectApplyFunction(new ResourceLocation("arcanearrays:functions/effect_apply"));
            function.setEffect(testEffect);
            functionCache.put(pos, function);
            array.addFunction(function);    
        }
        this.setChanged();
    }
    public void removeFunction(ArrayFunction function) {
        array.getFunctions().remove(function);
    }
    
    public MobEffect testForEffect(Level level, BlockPos pos, ISpellDefinition spell) {
        MobEffect holder = null;
        Spider testmob = new Spider(EntityType.SPIDER, level);
        SpellSource me = new SpellSource(owner == null ? null : level.getPlayerByUUID(owner), null, null, null);
        spell.getComponent(0).getPart().ApplyEffect(me, new SpellTarget(testmob), spell.getComponent(0), new SpellContext((ServerLevel)level, spell, testmob));
        if (!testmob.getActiveEffects().isEmpty()) {
            holder = testmob.getActiveEffectsMap().keySet().iterator().next();
            if (holder == ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("mna","chrono_anchor")))
                holder = null;
        }
        return holder;
    }
    
    public void instateArray() {
        this.array.setCaster(owner);
        this.level.getCapability(ArrayCapProvider.ARRAYS).ifPresent(w -> w.addArray(this.worldPosition, array));
    }
    public void revokeArray() {
        this.level.getCapability(ArrayCapProvider.ARRAYS).ifPresent(w -> w.removeArray(this.worldPosition, array));
    }
    
    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (owner != null) {
            nbt.putUUID("owner", owner);
        }
        if (!functionCache.isEmpty()) {
            ListTag fCache = new ListTag();
            functionCache.forEach((pos,func) -> {
                CompoundTag focus = new CompoundTag();
                focus.putLong("focus_pos", pos.asLong());
                focus.put("focus_function", func.toNBT());
                fCache.add(focus);
            });
            nbt.put("focus_cache", fCache);
        }
        if (array != null) {
            CompoundTag arrayData = new CompoundTag();
            this.array.writeToNBT(arrayData);
            nbt.put("arrayData", arrayData);
        }
    }
    
    @Override
    public void load(CompoundTag nbt) {
        if (nbt.contains("owner"))
            this.owner = nbt.getUUID("owner");
        if (nbt.contains("focus_cache")) {
            this.functionCache.clear();
            ListTag fCache = nbt.getList("focus_cache", Tag.TAG_COMPOUND);
            fCache.forEach(focusnbt -> {
                CompoundTag inbt = (CompoundTag)focusnbt;
                if (inbt.contains("focus_pos") && inbt.contains("focus_function")) {
                    functionCache.put(BlockPos.of(inbt.getLong("focus_pos")), ArrayFunction.fromNBT(inbt.getCompound("focus_function")));
                }   
            });
        }
        if (nbt.contains("arrayData"))
            this.array = ArrayDefinition.readFromNBT(nbt.getCompound("arrayData"));
        super.load(nbt);
    }
}