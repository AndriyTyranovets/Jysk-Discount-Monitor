package com.github.andriityranovets.jyskdiscount.repository;

import com.github.andriityranovets.jyskdiscount.model.ProductResponse;
import com.github.andriityranovets.jyskdiscount.model.Store;
import com.github.andriityranovets.jyskdiscount.model.StoreAvailability;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

public interface JyskRepository {
    ProductResponse getItemInfo(String article) throws URISyntaxException, IOException, InterruptedException;

    Map<String, Store> findStoreByCity(String city) throws URISyntaxException, IOException, InterruptedException;

    Map<String, StoreAvailability> getStoreAvailability(String article, Set<String> storeIDs) throws URISyntaxException, IOException, InterruptedException;
}
