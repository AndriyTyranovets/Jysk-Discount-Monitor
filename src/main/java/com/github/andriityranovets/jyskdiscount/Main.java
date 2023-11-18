package com.github.andriityranovets.jyskdiscount;

import com.github.andriityranovets.jyskdiscount.model.Product;
import com.github.andriityranovets.jyskdiscount.model.Store;
import com.github.andriityranovets.jyskdiscount.model.StoreAvailability;
import com.github.andriityranovets.jyskdiscount.notification.NotificationSender;
import com.github.andriityranovets.jyskdiscount.repository.JyskRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    private static final Set<String> HELP_OPTS = Set.of("-h", "--help");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy '('HH:mm')'");

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        if(parseArgs(args)) return;
        var article = args[0];
        var city = args[1];

        var module = new Module();

        checkDiscount(article, city, module);
    }

    private static void checkDiscount(String article, String city, Module module) throws URISyntaxException, IOException, InterruptedException {
        var repository = module.get(JyskRepository.class);
        var itemInfo = repository.getItemInfo(article);
        var stores = repository.findStoreByCity(city);
        var availability = repository.getStoreAvailability(article,
                stores.values()
                        .stream()
                        .map(Store::shopId)
                        .collect(Collectors.toUnmodifiableSet())
        );

        final Product item = itemInfo.data().get(0).product();
        printDiscount(stores, availability, item);
        final var notificationSender = module.get(NotificationSender.class);
        notificationSender.sendNotification(
                item.title(),
                item.price(),
                item.beforePrice() == null ? null : item.beforePrice().toString(),
                item.url(),
                item.campaignPriceEndDate(),
                stores.keySet()
                        .stream()
                        .filter(id -> availability.get(id).units() > 0)
                        .map(id -> "• " + availability.get(id).units() + " pcs @ " + stores.get(id).street() + " " + stores.get(id).house())
                        .toList()
        );
    }

    private static void printDiscount(Map<String, Store> stores, Map<String, StoreAvailability> availability, Product item) {
        if(item.hasDiscount()){
            System.out.println("Discount on " + item.title() + "!");
            System.out.println("Now for just " + item.price() + " (" + formatOldPrice(item.beforePrice().toString()) + ")");
            System.out.println(item.url());
            System.out.println("Available in stores:");
            stores.keySet()
                    .stream()
                    .map(id -> "• " + availability.get(id).units() + " pcs @ " + stores.get(id).street() + " " + stores.get(id).house())
                    .forEach(System.out::println);
            System.out.println("Offer till " + item.campaignPriceEndDate().format(FORMATTER));
        }
    }

    private static String formatOldPrice(String oldPrice) {
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

    private static boolean parseArgs(String[] args) {
        if(args.length == 1 && HELP_OPTS.contains(args[0])) {
            showHelp();
            return true;
        }
        if(args.length < 2) {
            System.err.println("Two argument -- Jysk product article and city -- are expected, but got just " + args.length);
            throw new IllegalArgumentException("Two argument -- Jysk product article and city -- are expected, but got just " + args.length);
        }
        if(args.length > 2) {
            System.err.println("Two argument -- Jysk product article and city -- are expected, but got " + args.length + ". If city consists of more than one word than use double quotes (\"<city>\").");
            throw new IllegalArgumentException("Two argument -- Jysk product article and city -- are expected, but got " + args.length + ". If city consists of more than one word than use double quotes (\"<city>\").");
        }
        if(args[0].length() < 3) {
            System.err.println("Article length is too small!");
            throw new IllegalArgumentException("Article length is too small!");
        }
        if(args[0].length() > 24) {
            System.err.println("Article length is too large!");
            throw new IllegalArgumentException("Article length is too large!");
        }
        if(!args[0].chars().allMatch(Character::isDigit)) {
            System.err.println("Article should contain digits only!");
            throw new IllegalArgumentException("Article should contain digits only!");
        }
        return false;
    }

    private static void showHelp() {
        System.out.println(".. <article> <city>");
        System.out.println("<article>\tproduct article; can be located on the product page just below product name");
        System.out.println("<city>\t\tcity where availability should be checked; just name in Ukrainian without prefixes (e.g. \"Київ\" instead of \"м. Київ\")");
    }
}
