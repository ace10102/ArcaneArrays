package com.spoilers.arcanearrays.rituals;

import com.mna.api.rituals.RitualEffect;
import com.spoilers.arcanearrays.ArcaneArrays;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AARitualRegistry {

    @SubscribeEvent
    public static void onRegisterRituals(RegistryEvent.Register<RitualEffect> event) {
        event.getRegistry().register(new RitualEffectCreateArray(new ResourceLocation(ArcaneArrays.MOD_ID, "rituals/create_array"))
                .setRegistryName(new ResourceLocation(ArcaneArrays.MOD_ID, "ritual-effect-create-array")));
        ArcaneArrays.LOGGER.info("Arcane Arrays: rituals registered");
    }
}