package com.spoilers.arcanearrays.arrays;

import com.spoilers.arcanearrays.ArcaneArrays;
import com.spoilers.arcanearrays.arrays.arrangements.ArrayArrangement;
import com.spoilers.arcanearrays.arrays.arrangements.DefensiveArrangement;
import com.spoilers.arcanearrays.arrays.arrangements.OffensiveArrangement;
import com.spoilers.arcanearrays.arrays.arrangements.UtilityArrangement;
import com.spoilers.arcanearrays.arrays.functions.ArrayFunction;
import com.spoilers.arcanearrays.arrays.functions.BarrierFunction;
import com.spoilers.arcanearrays.arrays.functions.EffectApplyFunction;
import com.spoilers.arcanearrays.arrays.functions.EffectPreventFunction;
import com.spoilers.arcanearrays.arrays.functions.NoBreakingFunction;
import com.spoilers.arcanearrays.arrays.functions.NoExplosionFunction;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ArrayModulesInit {
    //arrangements
    public static final ArrayArrangement DEFENSIVE = new DefensiveArrangement(new ResourceLocation(ArcaneArrays.MOD_ID,"arrangements/defensive"));
    public static final ArrayArrangement OFFENSIVE = new OffensiveArrangement(new ResourceLocation(ArcaneArrays.MOD_ID,"arrangements/offensive"));
    public static final ArrayArrangement UTILITY = new UtilityArrangement(new ResourceLocation(ArcaneArrays.MOD_ID,"arrangements/utility"));
    //functions
    public static final ArrayFunction BARRIER = new BarrierFunction(new ResourceLocation(ArcaneArrays.MOD_ID,"functions/barrier"));
    public static final ArrayFunction NOEXPLOSIONS = new NoExplosionFunction(new ResourceLocation(ArcaneArrays.MOD_ID,"functions/no_explosion"));
    public static final ArrayFunction NOBREAKING = new NoBreakingFunction(new ResourceLocation(ArcaneArrays.MOD_ID,"functions/no_breaking"));
    public static final ArrayFunction EFFECTAPPLY=  new EffectApplyFunction(new ResourceLocation(ArcaneArrays.MOD_ID,"functions/effect_apply"));
    public static final ArrayFunction EFFECTPREVENT = new EffectPreventFunction(new ResourceLocation(ArcaneArrays.MOD_ID,"functions/effect_prevent"));


    @SubscribeEvent
    public static void registerArrangements(RegistryEvent.Register<ArrayArrangement> event) {
        event.getRegistry().registerAll(new ArrayArrangement[]{DEFENSIVE, OFFENSIVE, UTILITY});
    }
    @SubscribeEvent
    public static void registerFunctions(RegistryEvent.Register<ArrayFunction> event) {
        event.getRegistry().registerAll(new ArrayFunction[]{BARRIER, NOEXPLOSIONS, NOBREAKING, EFFECTAPPLY, EFFECTPREVENT});
    }
}