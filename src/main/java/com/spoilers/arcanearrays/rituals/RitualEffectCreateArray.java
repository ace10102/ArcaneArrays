package com.spoilers.arcanearrays.rituals;

import java.util.ArrayList;
import java.util.Arrays;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.spoilers.arcanearrays.arrays.ArrayModuleRegistry;
import com.spoilers.arcanearrays.arrays.arrangements.ArrayArrangement;
import com.spoilers.arcanearrays.blocks.AABlockRegistry;
import com.spoilers.arcanearrays.blocks.tiles.ArrayTile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualEffectCreateArray extends RitualEffect {

    public RitualEffectCreateArray(ResourceLocation ritualName) {
        super(ritualName);   
    }
    
    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        BlockPos[] pedestalPos = new BlockPos[] {context.getCenter().offset(3, 0, 0), context.getCenter().offset(-3, 0, 0), context.getCenter().offset(0, 0, -3), context.getCenter().offset(0, 0, 3)};
        Block pedestal = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mna:pedestal"));
        ArrayList<ArrayArrangement> arrangementHolder = new ArrayList<ArrayArrangement>();
        for (BlockPos pos : pedestalPos) {
            if (!context.getWorld().getBlockState(pos).is(pedestal)) 
                return false;
            IItemHandler contents = context.getWorld().getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent() ? 
                    context.getWorld().getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get() : null;
            if (contents == null) 
                return false;
            Item pedestalItem = contents.getStackInSlot(0).getItem();
            ArrayModuleRegistry.Arrangements.get().forEach(arrangement -> {
                if (arrangement.getArrangementRecipeTag() != null && arrangement.getArrangementRecipeTag().contains(pedestalItem))
                    arrangementHolder.add(arrangement);
            });
        }
        System.out.println(arrangementHolder);
        if(arrangementHolder.size() < 4) return false;
        ArrayArrangement tester = arrangementHolder.get(0);
        arrangementHolder.removeIf(element -> element.equals(tester));
        if (!arrangementHolder.isEmpty()) return false;
        Arrays.asList(pedestalPos).forEach(pos -> {
            context.getWorld().getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get().extractItem(0, 1, false);
            context.getWorld().setBlock(pos, AABlockRegistry.ARRAY_FOCUS.get().defaultBlockState(), 3);
        });
        context.getWorld().setBlock(context.getCenter(), AABlockRegistry.ARRAY_CORE.get().defaultBlockState(), 3);
        ArrayTile array = context.getWorld().getBlockEntity(context.getCenter()) instanceof ArrayTile ? (ArrayTile)context.getWorld().getBlockEntity(context.getCenter()) : null;
        if (array == null) return false;
        array.setOwner(context.getCaster().getUUID());
        array.getArray().setArrangement(tester);
        return true;
    }
    
    @Override
    protected boolean modifyRitualReagentsAndPatterns(ItemStack dataStack, IRitualContext context) {
        context.replacePatterns(getArrangementWeaves());
        return true;
    }
    
    @Override
    public Component canRitualStart(IRitualContext context) {
        BlockPos[] pedestalPos = new BlockPos[] {context.getCenter().offset(3, 0, 0), context.getCenter().offset(-3, 0, 0), context.getCenter().offset(0, 0, -3), context.getCenter().offset(0, 0, 3)};
        Block pedestal = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mna:pedestal"));
        ArrayList<ArrayArrangement> arrangementHolder = new ArrayList<ArrayArrangement>();
        for (BlockPos pos : pedestalPos) {
            if (!context.getWorld().getBlockState(pos).is(pedestal)) {
                return new TranslatableComponent("ritual.arcanearrays.no_pedestal", pos.toShortString());
            }
            IItemHandler contents = context.getWorld().getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent() ? 
                    context.getWorld().getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get() : null;
            if (contents == null || contents.getStackInSlot(0).getItem().equals(Items.AIR)) 
                return new TranslatableComponent("ritual.arcanearrays.no_pedestal_item", pos.toShortString());
            Item pedestalItem = contents.getStackInSlot(0).getItem();
            ArrayModuleRegistry.Arrangements.get().forEach(arrangement -> {
                if (arrangement.getArrangementRecipeTag() != null && arrangement.getArrangementRecipeTag().contains(pedestalItem))
                    arrangementHolder.add(arrangement);
            });
        }
        System.out.println(arrangementHolder.toString());
        if(arrangementHolder.size() < 4) return new TranslatableComponent("ritual.arcanearrays.wrong_pedestal_item");
        ArrayArrangement tester = arrangementHolder.get(0);
        arrangementHolder.removeIf(element -> element.equals(tester));
        if (!arrangementHolder.isEmpty()) return new TranslatableComponent("ritual.arcanearrays.wrong_pedestal_item");
        return null;
    }
    
    private NonNullList<ResourceLocation> getArrangementWeaves() {
        NonNullList<ResourceLocation> weaves = NonNullList.create();
        weaves.add(new ResourceLocation("mna:manaweave_patterns/circle"));
        weaves.add(new ResourceLocation("mna:manaweave_patterns/square"));
        weaves.add(new ResourceLocation("mna:manaweave_patterns/diamond"));
        weaves.add(new ResourceLocation("mna:manaweave_patterns/triangle"));
        weaves.add(new ResourceLocation("mna:manaweave_patterns/inverted_triangle"));
        return weaves;
    }
}