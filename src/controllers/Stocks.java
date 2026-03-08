package controllers;

import java.util.HashMap;
import java.util.Map;

public class Stocks {
    public static Stocks instance = new Stocks();
    public static Map<String, Integer> stocks = new HashMap<>(Map.of(
            "Patate", 0,
            "Maïs", 0
    ));

    public void add(String plant, int qty) {
        stocks.put(plant, stocks.get(plant) + qty);
    }
}

