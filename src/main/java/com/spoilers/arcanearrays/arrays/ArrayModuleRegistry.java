package com.spoilers.arcanearrays.arrays;

import java.util.function.Supplier;

import com.spoilers.arcanearrays.arrays.arrangements.ArrayArrangement;
import com.spoilers.arcanearrays.arrays.functions.ArrayFunction;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public class ArrayModuleRegistry {

    public static Supplier<IForgeRegistry<ArrayArrangement>> Arrangements;
    public static Supplier<IForgeRegistry<ArrayFunction>> Functions;
    
    @SubscribeEvent
    public static void RegisterRegistries(NewRegistryEvent event) {
        RegistryBuilder<ArrayArrangement> rbArrangements = new RegistryBuilder<ArrayArrangement>();
        rbArrangements.setName(new ResourceLocation("arcanearrays:arrangements")).setType(ArrayArrangement.class).set(new IForgeRegistry.DummyFactory<ArrayArrangement>(){

            public ArrayArrangement createDummy(ResourceLocation key) {
                return new ArrayArrangement.PhantomArrangement();
            }
        }).set(new IForgeRegistry.MissingFactory<ArrayArrangement>(){

            public ArrayArrangement createMissing(ResourceLocation key, boolean isNetwork) {
                return new ArrayArrangement.PhantomArrangement();
            }
        }).disableSaving().allowModification();
        Arrangements = event.create(rbArrangements);
        
        RegistryBuilder<ArrayFunction> rbFunctions = new RegistryBuilder<ArrayFunction>();
        rbFunctions.setName(new ResourceLocation("arcanearrays:functions")).setType(ArrayFunction.class).set(new IForgeRegistry.DummyFactory<ArrayFunction>(){

            public ArrayFunction createDummy(ResourceLocation key) {
                return new ArrayFunction.PhantomFunction();
            }
        }).set(new IForgeRegistry.MissingFactory<ArrayFunction>(){

            public ArrayFunction createMissing(ResourceLocation key, boolean isNetwork) {
                return new ArrayFunction.PhantomFunction();
            }
        }).disableSaving().allowModification();
        Functions = event.create(rbFunctions);
    }
}
