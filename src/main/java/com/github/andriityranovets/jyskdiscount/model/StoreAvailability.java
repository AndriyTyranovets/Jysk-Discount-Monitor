package com.github.andriityranovets.jyskdiscount.model;

import java.time.Instant;

public record StoreAvailability(
        String shopId,
        int units,
        String measure,
        boolean onDisplay,
        Instant timestamp
) { }
