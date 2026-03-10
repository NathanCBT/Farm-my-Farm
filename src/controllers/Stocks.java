package controllers;

import java.util.HashMap;
import java.util.Map;

public class Stocks {
    public static Stocks instance = new Stocks();
    public static double money = 500.0; // le solde actuel 

    public static Map<String, Integer> stocks = new HashMap<>(Map.of(
            "Patate", 0,
            "Maïs", 0
    ));

    public void add(String plant, int qty) {
        stocks.put(plant, stocks.get(plant) + qty);
    }

    // Méthode pour vendre
    public double sellPlant(String plantName, double pricePerUnit) {
        int qty = stocks.get(plantName);
        if (qty > 0) {
            double gain = qty * pricePerUnit;
            money += gain;
            stocks.put(plantName, 0); // vide le stock après vente
            return gain; // puis on retourne le gain pour l'afficher
        }
        return 0;
    }
}