package com.github.andriityranovets.jyskdiscount.model.adapters;

import com.github.andriityranovets.jyskdiscount.model.ProductNode;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class ProductNodeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if(type.getRawType() != ProductNode.class) {
            return null;
        }
        return (TypeAdapter<T>) new ProductNodeAdapter(gson);
    }
}
