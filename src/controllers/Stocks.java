package controllers;

import java.util.HashMap;
import java.util.Map;

public class Stocks {
    public static Stocks instance = new Stocks();
    public static double money = 200.0;

    public static Map<String, Integer> animals = new HashMap<>(Map.of(
            "Poule", 0,
            "Vache", 0
    ));

    public static Map<String, Integer> animalProducts = new HashMap<>(Map.of(
            "Oeuf", 0,
            "Lait", 0
    ));

    public static Map<String, Integer> harvests = new HashMap<>(Map.of(
            "Patate", 0,
            "Maïs", 0
    ));

    public static Map<String, Integer> seeds = new HashMap<>(Map.of(
            "Patate", 5,
            "Maïs", 2
    ));

    // ajouter des graines
    public void addSeeds(String plant, int qty) {
        seeds.put(plant, seeds.get(plant) + qty);
    }

    // ajouter à la récolte
    public void addHarvest(String plant, int qty) {
        harvests.put(plant, harvests.get(plant) + qty);
    }

    // vendre la RÉCOLTE
    public double sellHarvest(String plantName, double pricePerUnit) {
        int qty = harvests.getOrDefault(plantName, 0);

        if (qty > 0) {
            double gain = qty * pricePerUnit;
            money += gain;
            harvests.put(plantName, 0); // vide le stock de récolte après vente
            return gain;
        }
        return 0;
    }
}