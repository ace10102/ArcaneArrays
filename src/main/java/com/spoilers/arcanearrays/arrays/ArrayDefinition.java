package com.spoilers.arcanearrays.arrays;

import java.util.ArrayList;
import java.util.UUID;

import com.spoilers.arcanearrays.arrays.arrangements.ArrayArrangement;
import com.spoilers.arcanearrays.arrays.functions.ArrayFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class ArrayDefinition {
    
    public long position;
    private UUID casterUUID;
    public AABB aoe;
    public static final short default_range = 32;
    public short range;
    
    
    public ArrayArrangement arrangement;
    public ArrayList<ArrayFunction> functions;
    //public ArrayList<ArrayResonator> resonances;//todo
    
    public ArrayDefinition() {
        this.functions = new ArrayList<ArrayFunction>(4);
    }
    
    public ArrayDefinition(Player pCaster, BlockPos location) {
        this.casterUUID = pCaster.getUUID();
        this.position = location.asLong();
        this.aoe = new AABB(location).inflate(default_range);
        this.functions = new ArrayList<ArrayFunction>(4);
    }
    
    public BlockPos getPos() {
        return BlockPos.of(position);
    }
    public void setPos(BlockPos pos) {
        this.position = pos.asLong();
    }
    public void setPos(Long pos) {
        this.position = pos;
    }
    
    public AABB getArea() {
        return aoe == null ? new AABB(BlockPos.of(position)).inflate(default_range) : aoe;
    }
    
    public UUID getCaster() {
        return this.casterUUID;
    }
    public void setCaster(UUID caster) {
        this.casterUUID = caster;
    }
    
    public ArrayArrangement getArrangement() {
        return this.arrangement;
    }
    public void setArrangement(ArrayArrangement pArrangement) {
        this.arrangement = pArrangement;
    }
    
    public ArrayList<ArrayFunction> getFunctions() {
        return this.functions;
    }
    public void addFunction(ArrayFunction function) {
        if (!this.functions.contains(function))
            this.functions.add(function);
    }
    
    
    public void writeToNBT(CompoundTag nbt) {
        CompoundTag array = new CompoundTag();
        if(this.position != 0)
            array.putLong("position", this.position);
        if(this.casterUUID != null)
            array.putUUID("caster", this.casterUUID);
        if(this.arrangement != null)
            array.putString("arrangement", this.arrangement.getRegistryName().toString());
        CompoundTag functionData = new CompoundTag();
        if(this.functions != null && !this.functions.isEmpty()) {
            for(int i = 0; i < functions.size(); i++ ) {
                functionData.put("function_"+i, this.functions.get(i).toNBT());
            }
            array.put("functions", functionData);
        }
        nbt.put("array", array);
    }
    
    public static <F extends ArrayFunction> ArrayDefinition readFromNBT(CompoundTag nbt) {
        if (nbt == null || !nbt.contains("array")) {
            return null;
        }
        ArrayDefinition array = new ArrayDefinition();
        CompoundTag arrayTag = nbt.getCompound("array");
        if (arrayTag.contains("position"))
            array.position = arrayTag.getLong("position");
        if (arrayTag.contains("caster")) 
            array.casterUUID = arrayTag.getUUID("caster");
        if (arrayTag.contains("arrangement"))
            array.arrangement = ArrayModuleRegistry.Arrangements.get().getValue(new ResourceLocation(arrayTag.getString("arrangement")));
        if (arrayTag.contains("functions")) {
            CompoundTag functionsTag = arrayTag.getCompound("functions");
            for (int i = 0; i <= 3; ++i) {
                if (functionsTag.contains("function_"+i)) {
                    CompoundTag function = functionsTag.getCompound("function_"+i);
                    F aFunction = ArrayFunction.fromNBT(function);
                    array.addFunction(aFunction);
                } else break;
            }
        }
        return array;
    }
}