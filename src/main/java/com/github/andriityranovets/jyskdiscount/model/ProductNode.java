package com.github.andriityranovets.jyskdiscount.model;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.UUID;

@RecordBuilder
public record ProductNode(
        UUID id,
        String type,
        Product product
) { }
