package com.github.andriityranovets.jyskdiscount.model.adapters;

import com.github.andriityranovets.jyskdiscount.model.Product;
import com.github.andriityranovets.jyskdiscount.model.ProductNode;
import com.github.andriityranovets.jyskdiscount.model.ProductNodeBuilder;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class ProductNodeAdapter extends TypeAdapter<ProductNode> {
    private static final String KEY_TYPE = "type";
    private static final String KEY_ID = "id";
    private static final String KEY_ATTRIBUTES = "attributes";
    private static final String KEY_PRODUCT_TEASER_JUI_JSON = "product_teaser_jui_json";
    private static final String KEY_VALUE = "value";

    private final Gson gson;

    public ProductNodeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter writer, ProductNode value) {
        throw new RuntimeException("ProductNodeAdapter#write() not yet implemented.");
    }

    @Override
    public ProductNode read(JsonReader reader) throws IOException {
        reader.beginObject();
        var builder = ProductNodeBuilder.builder();
        var token = reader.peek();
        String name;
        while (token != JsonToken.END_OBJECT) {
            if(token == JsonToken.NAME) {
                name = reader.nextName();
                switch(name) {
                    case KEY_ID -> builder.id(UUID.fromString(reader.nextString()));
                    case KEY_TYPE -> builder.type(reader.nextString());
                    case KEY_ATTRIBUTES -> {
                        var productJson = findProductTeaserJuiJson(reader);
                        var product = gson.fromJson(productJson, Product.class);
                        builder.product(product);
                    }
                    default -> reader.skipValue();
                }
            }
            token = reader.peek();
        }
        reader.endObject();
        return builder.build();
    }

    private String findProductTeaserJuiJson(JsonReader reader) throws IOException {
        String value = null;
        reader.beginObject();
        String name;
        var token = reader.peek();
        while(token != JsonToken.END_OBJECT) {
            name = reader.nextName();
            if(KEY_PRODUCT_TEASER_JUI_JSON.equals(name)) {
                value = findValue(reader);
            } else {
                reader.skipValue();
            }
            token = reader.peek();
        }
        reader.endObject();
        return value;
    }

    private String findValue(JsonReader reader) throws IOException {
        String value = null;
        reader.beginObject();
        String name;
        var token = reader.peek();
        while(token != JsonToken.END_OBJECT) {
            name = reader.nextName();
            if(KEY_VALUE.equals(name)) {
                value = reader.nextString();
            } else {
                reader.skipValue();
            }
            token = reader.peek();
        }
        reader.endObject();
        return value;
    }
}
