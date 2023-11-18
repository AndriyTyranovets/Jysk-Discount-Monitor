package com.github.andriityranovets.jyskdiscount.repository;

import com.github.andriityranovets.jyskdiscount.model.Availability;
import com.github.andriityranovets.jyskdiscount.model.ProductResponse;
import com.github.andriityranovets.jyskdiscount.model.Store;
import com.github.andriityranovets.jyskdiscount.model.StoreAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JyskRepositoryImpl implements JyskRepository {
    private static final String GET_PRODUCT = "https://jysk.ua/jsonapi/node/product?filter[field_catalog]=PC_COM_UA&filter[field_external_key][condition][path]=field_external_key&filter[field_external_key][condition][value][0]=%1$s&filter[field_external_key][condition][value][1]=%1$s&filter[field_external_key][condition][value][2]=%1$s&filter[field_external_key][condition][operator]=IN&fields[node--product]=product_teaser_jui_json";
    private static final String GET_STORES = "https://jysk.ua/services/stores/get?amount=all&lat=48.3794&lon=31.1656&sort_by=distance";
    private static final String GET_AVAILABILITY_IN_STORES = "https://jysk.ua/websapapi/shops/atp/1017/%s?negativeOk=1";

    private final Gson gson;

    public JyskRepositoryImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public ProductResponse getItemInfo(String article) throws URISyntaxException, IOException, InterruptedException {
        var uri = new URI(String.format(GET_PRODUCT, article));
        var req = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        var response = HttpClient.newHttpClient()
                .send(req, HttpResponse.BodyHandlers.ofInputStream())
                .body();

        return gson.fromJson(new InputStreamReader(response), ProductResponse.class);
    }

    @Override
    public Map<String, Store> findStoreByCity(String city) throws URISyntaxException, IOException, InterruptedException {
        var uri = new URI(GET_STORES);
        var req = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        var response = HttpClient.newHttpClient()
                .send(req, HttpResponse.BodyHandlers.ofInputStream())
                .body();

        var type = new TypeToken<Map<String, Store>>() {}.getType();
        Map<String, Store> stores = gson.fromJson(new InputStreamReader(response), type);

        return stores.entrySet().stream()
                .filter(e -> e.getValue().city().contains(city))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, StoreAvailability> getStoreAvailability(String article, Set<String> storeIDs) throws URISyntaxException, IOException, InterruptedException {
        var uri = new URI(String.format(GET_AVAILABILITY_IN_STORES, article));
        var req = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        var response = HttpClient.newHttpClient()
                .send(req, HttpResponse.BodyHandlers.ofInputStream())
                .body();

        var availabilityInStores = gson.fromJson(new InputStreamReader(response), Availability.class).availability();
        return storeIDs.stream()
                .filter(availabilityInStores::containsKey)
                .collect(Collectors.toMap(Function.identity(), availabilityInStores::get));
    }
}
