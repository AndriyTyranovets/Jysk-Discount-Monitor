package com.github.andriityranovets.jyskdiscount.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public record Store(
        @SerializedName("nid") int id,
        @SerializedName("shop_name") String shopName,
        @SerializedName("shop_id") String shopId,
        @SerializedName("street") String street,
        @SerializedName("house") String house,
        @SerializedName("zipcode") String zipCode,
        @SerializedName("city") String city,
        @SerializedName("opening") Map<Integer, OpeningHours> openingHours
) { }
