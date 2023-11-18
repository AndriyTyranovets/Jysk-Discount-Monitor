package com.github.andriityranovets.jyskdiscount.model.adapters;

import com.github.andriityranovets.jyskdiscount.model.Product;
import com.github.andriityranovets.jyskdiscount.model.ProductBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ProductAdapter extends TypeAdapter<Product> {
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String KEY_WSS_ID = "wssId";
    private static final String KEY_PRICE = "price";
    private static final String KEY_MIN_SINGLE_PRICE = "minSinglePrice";
    private static final String KEY_BEFORE_PRICE = "beforePrice";
    private static final String KEY_SHOW_DISCOUNT = "showDiscount";
    private static final String KEY_DISCOUNT = "discount";
    private static final String KEY_ONLINE_SALES = "onlineSales";
    private static final String KEY_ONLINE_ONLY = "onlineOnly";
    private static final String KEY_STORE_SALES = "storeSales";
    private static final String KEY_ONLY_IN_STORE = "onlyInStore";
    private static final String KEY_CAMPAIGN_PRICE_END_DATE = "campaignPriceEndDate";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static final Pattern P_BEFORE_PRICE = Pattern.compile("(\\d+) .*");
    private static final String JYSK_URL_BASE = "https://jysk.ua";

    @Override
    public void write(JsonWriter writer, Product value) {
        throw new RuntimeException("ProductAdapter#write() not yet implemented.");
    }

    @Override
    public Product read(JsonReader reader) throws IOException {
        var builder = ProductBuilder.builder();
        reader.beginObject();
        var token = reader.peek();
        String name;
        while (token != JsonToken.END_OBJECT) {
            if(token == JsonToken.NAME) {
                name = reader.nextName();
                switch(name) {
                    case KEY_TITLE -> builder.title(reader.nextString());
                    case KEY_URL -> {
                        var url = reader.nextString();
                        builder.url(url == null ? null : JYSK_URL_BASE + url);
                    }
                    case KEY_WSS_ID -> builder.article(reader.nextString());
                    case KEY_PRICE -> findPrice(builder, reader);
                    case KEY_SHOW_DISCOUNT -> builder.hasDiscount(reader.nextBoolean());
                    case KEY_DISCOUNT -> builder.discount(reader.nextInt());
                    case KEY_ONLINE_SALES -> builder.onlineSales(reader.nextBoolean());
                    case KEY_ONLINE_ONLY -> builder.onlineOnly(reader.nextBoolean());
                    case KEY_STORE_SALES -> builder.storeSales(reader.nextBoolean());
                    case KEY_ONLY_IN_STORE -> builder.inStoreOnly(reader.nextBoolean());
                    case KEY_CAMPAIGN_PRICE_END_DATE -> {
                        token = reader.peek();
                        if(token == JsonToken.NULL) {
                            reader.nextNull();
                            builder.campaignPriceEndDate(null);
                        } else {
                            builder.campaignPriceEndDate(LocalDateTime.parse(reader.nextString(), DATE_TIME_FORMATTER));
                        }
                    }
                    default -> reader.skipValue();
                }
            }
            token = reader.peek();
        }
        reader.endObject();
        return builder.build();
    }

    private void findPrice(ProductBuilder builder, JsonReader reader) throws IOException {
        reader.beginObject();
        String name;
        var token = reader.peek();
        while (token != JsonToken.END_OBJECT) {
            if(token == JsonToken.NAME) {
                name = reader.nextName();
                switch(name) {
                    case KEY_MIN_SINGLE_PRICE -> builder.price(reader.nextInt());
                    case KEY_BEFORE_PRICE -> {
                        token = reader.peek();
                        if(token == JsonToken.STRING) {
                            var matcher = P_BEFORE_PRICE.matcher(reader.nextString());
                            if (matcher.find()) {
                                builder.beforePrice(Integer.parseInt(matcher.group(1)));
                            }
                        } else {
                            reader.nextNull();
                            builder.beforePrice(null);
                        }
                    }
                    default -> reader.skipValue();
                }
            }
            token = reader.peek();
        }
        reader.endObject();
    }
}
