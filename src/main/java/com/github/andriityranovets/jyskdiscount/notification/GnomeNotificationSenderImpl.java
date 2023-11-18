package com.github.andriityranovets.jyskdiscount.notification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GnomeNotificationSenderImpl implements NotificationSender {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy '('HH:mm')'");

    @Override
    public void sendNotification(String title, int price, String oldPrice, String url, LocalDateTime offerUntil, List<String> storeAvailability) throws IOException {
        final int linesTotal = 4 + storeAvailability.size();
        final String[] lines = new String[linesTotal];
        lines[0] = "Now for just " + price + (oldPrice == null ? "" : " (" + formatOldPrice(oldPrice) + ")");
        lines[1] = "Offer valid till " + offerUntil.format(FORMATTER);
        lines[2] = storeAvailability.size() == 0 ? "Not available in stores nearby" : "Available in stores:";
        for(int i = 0; i < storeAvailability.size(); ++i) {
            lines[3+i] = storeAvailability.get(i);
        }
        lines[lines.length-1] = "Link: " + url;
        new ProcessBuilder(
                "notify-send",
                "--icon=software-update-urgent-symbolic",
                "--urgency=critical",
                "«" + title + "» is on sale!",
                String.join("\n", lines))
                .inheritIO()
                .start();
    }

    private String formatOldPrice(String oldPrice) {
        return oldPrice
                .chars()
                .boxed()
                .reduce(
                        new StringBuilder(),
                        (sub, el) -> sub.append("\u0336").append(Character.toChars(el)),
                        StringBuilder::append
                )
                .append("\u0336")
                .toString();
    }
}