package com.spoilers.arcanearrays.capabilities;

import com.spoilers.arcanearrays.ArcaneArrays;
import com.spoilers.arcanearrays.capabilities.world.ArrayRegistryCapability;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityRegistry {
     
    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ArrayRegistryCapability.class);
        ArcaneArrays.LOGGER.info("A&A -> Capabilities Registered");
    } 
}