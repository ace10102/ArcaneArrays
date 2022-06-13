package com.spoilers.arcanearrays.capabilities.world;

import java.util.concurrent.Callable;

public final class ArrayCapFactory
implements Callable<ArrayCap> {
    @Override
    public ArrayCap call() throws Exception {
        return new ArrayCap();
    }
}

