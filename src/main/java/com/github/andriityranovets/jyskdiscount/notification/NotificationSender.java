package com.github.andriityranovets.jyskdiscount.notification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationSender {
    void sendNotification(String title, int price, String oldPrice, String url, LocalDateTime offerUntil, List<String> storeAvailability) throws IOException;
}
