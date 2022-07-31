package com.spoilers.arcanearrays.capabilities.world;

import java.util.concurrent.Callable;

public final class ArrayCapFactory implements Callable<ArrayRegistryCapability> {
    @Override
    public ArrayRegistryCapability call() throws Exception {
        return new ArrayRegistryCapability();
    }
}