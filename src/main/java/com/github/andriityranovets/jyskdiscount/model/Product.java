package com.github.andriityranovets.jyskdiscount.model;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.LocalDateTime;

@RecordBuilder
public record Product(
        String title,
        String url,
        String article,
        int price,
        Integer beforePrice,
        boolean hasDiscount,
        int discount,
        boolean onlineSales,
        boolean onlineOnly,
        boolean storeSales,
        boolean inStoreOnly,
        LocalDateTime campaignPriceEndDate
) { }
