package com.github.andriityranovets.jyskdiscount.model.adapters;

import com.github.andriityranovets.jyskdiscount.model.Availability;
import com.github.andriityranovets.jyskdiscount.model.StoreAvailability;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;

public class AvailabilityAdapter extends TypeAdapter<Availability> {
    private static final String KEY_TOTAL_NUMBER_STORES_WITH_ATP = "totalNumberStoresWithAtp";
    private static final String KEY_TOTAL_NUMBER_STORES = "totalNumberStores";
    private final Gson gson;

    public AvailabilityAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter writer, Availability value) {
        throw new RuntimeException("AvailabilityAdapter#write() not yet implemented.");
    }

    @Override
    public Availability read(JsonReader reader) throws IOException {
        var availabilityList = new HashMap<String, StoreAvailability>();
        reader.beginObject();
        var token = reader.peek();
        String name;
        while (token != JsonToken.END_OBJECT) {
            if(token == JsonToken.NAME) {
                name = reader.nextName();
                switch(name) {
                    case KEY_TOTAL_NUMBER_STORES_WITH_ATP, KEY_TOTAL_NUMBER_STORES -> reader.skipValue();
                    default -> {
                        StoreAvailability storeAvailability = gson.fromJson(reader, StoreAvailability.class);
                        availabilityList.put(storeAvailability.shopId(), storeAvailability);
                    }
                }
            }
            token = reader.peek();
        }
        reader.endObject();
        return new Availability(availabilityList);
    }
}
