package com.github.andriityranovets.jyskdiscount.model;

import java.util.Map;

public record Availability(
        Map<String, StoreAvailability> availability
) { }
