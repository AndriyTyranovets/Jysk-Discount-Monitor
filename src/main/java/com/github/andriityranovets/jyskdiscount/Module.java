package com.github.andriityranovets.jyskdiscount;

import com.github.andriityranovets.jyskdiscount.model.OpeningHours;
import com.github.andriityranovets.jyskdiscount.model.Product;
import com.github.andriityranovets.jyskdiscount.model.adapters.*;
import com.github.andriityranovets.jyskdiscount.notification.GnomeNotificationSenderImpl;
import com.github.andriityranovets.jyskdiscount.notification.NotificationSender;
import com.github.andriityranovets.jyskdiscount.repository.JyskRepository;
import com.github.andriityranovets.jyskdiscount.repository.JyskRepositoryImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.ProviderNotFoundException;
import java.time.Instant;
import java.util.Map;
import java.util.function.Supplier;

public class Module {
    private Map<Class, Supplier> beanFactories = Map.of(
            JyskRepository.class, this::getJyskRepository,
            Gson.class, this::getGson,
            NotificationSender.class, this::getNotificationSender
    );

    public <T> T get(Class<T> clazz) {
        if(beanFactories.containsKey(clazz)) {
            var bean = beanFactories.get(clazz).get();
            if(clazz.isInstance(bean)) {
                return clazz.cast(bean);
            }
        }
        throw new ProviderNotFoundException("No bean of type " + clazz.getName());
    }

    private JyskRepository getJyskRepository() {
        return new JyskRepositoryImpl(get(Gson.class));
    }

    private NotificationSender getNotificationSender() {
        return new GnomeNotificationSenderImpl();
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new ProductNodeAdapterFactory())
                .registerTypeAdapterFactory(new AvailabilityAdapterFactory())
                .registerTypeAdapter(Product.class, new ProductAdapter())
                .registerTypeAdapter(OpeningHours.class, new OpeningHoursAdapter())
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }
}
