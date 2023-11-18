package com.github.andriityranovets.jyskdiscount.model;

import java.util.List;

public record ProductResponse(
        List<ProductNode> data
) { }
