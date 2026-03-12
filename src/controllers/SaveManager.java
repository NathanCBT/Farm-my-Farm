package controllers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import Animals.Animal;
import Animals.Poule;
import Animals.Vache;
import Plants.Plant;
import Plants.Patate;
import Plants.Mais;

public class SaveManager {
    private static final String FILE_PATH = "save_farm.json";

    public static void saveGame() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"money\": ").append(Stocks.money).append(",\n");
        json.append("  \"seeds\": ").append(mapToJson(Stocks.seeds)).append(",\n");
        json.append("  \"harvests\": ").append(mapToJson(Stocks.harvests)).append(",\n");
        json.append("  \"animals\": ").append(mapToJson(Stocks.animals)).append(",\n");
        json.append("  \"animalProducts\": ").append(mapToJson(Stocks.animalProducts)).append(",\n");

        // Sauvegarde du terrain (Plantes ET Animaux)
        json.append("  \"land\": [");
        boolean first = true;
        for (int r = 0; r < 20; r++) {
            for (int c = 0; c < 20; c++) {
                // Sauvegarde des plantes posées
                if (LandFarm.plantedSeeds[r][c] != null) {
                    if (!first) json.append(",");
                    json.append("{\"r\":").append(r).append(",\"c\":").append(c)
                            .append(",\"type\":\"plant\",\"t\":\"").append(LandFarm.plantedSeeds[r][c].name).append("\"")
                            .append(",\"ready\":").append(LandFarm.plantedSeeds[r][c].collectAuthorized).append("}");
                    first = false;
                }
                // Sauvegarde des animaux posés
                if (LandFarm.animalPens[r][c] != null) {
                    if (!first) json.append(",");
                    json.append("{\"r\":").append(r).append(",\"c\":").append(c)
                            .append(",\"type\":\"animal\",\"t\":\"").append(LandFarm.animalPens[r][c].name).append("\"")
                            .append(",\"ready\":").append(LandFarm.animalPens[r][c].canCollect).append("}");
                    first = false;
                }
            }
        }
        json.append("]\n}");

        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH))) {
            out.write(json.toString());
            System.out.println("Sauvegarde terrain + inventaire réussie !");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void loadGame() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Stocks.money = Double.parseDouble(extractValue(content, "\"money\":"));

            parseMap(content, "\"seeds\":", Stocks.seeds);
            parseMap(content, "\"harvests\":", Stocks.harvests);
            parseMap(content, "\"animals\":", Stocks.animals);
            parseMap(content, "\"animalProducts\":", Stocks.animalProducts);

            // Reset des tableaux
            for(int r=0; r<20; r++) {
                for(int c=0; c<20; c++) {
                    LandFarm.plantedSeeds[r][c] = null;
                    LandFarm.animalPens[r][c] = null;
                }
            }

            if (content.contains("\"land\":")) {
                int startIdx = content.indexOf("[", content.indexOf("\"land\":")) + 1;
                int endIdx = content.lastIndexOf("]");
                String section = content.substring(startIdx, endIdx).trim();

                if (!section.isEmpty()) {
                    String[] entries = section.split("\\},");
                    for (String entry : entries) {
                        if (!entry.endsWith("}")) entry += "}";

                        int r = Integer.parseInt(extractValue(entry, "\"r\":"));
                        int c = Integer.parseInt(extractValue(entry, "\"c\":"));
                        String typeObj = extractValue(entry, "\"type\":").replace("\"", "");
                        String name = extractValue(entry, "\"t\":").replace("\"", "");
                        boolean ready = Boolean.parseBoolean(extractValue(entry, "\"ready\":"));

                        if (typeObj.equals("plant")) {
                            Plant p = name.equals("Patate") ? new Patate() : new Mais();
                            p.collectAuthorized = ready;
                            LandFarm.plantedSeeds[r][c] = p;
                        } else if (typeObj.equals("animal")) {
                            Animal a = name.equals("Poule") ? new Poule() : new Vache();
                            a.canCollect = ready;
                            // On peut aussi charger l'état de faim si besoin
                            a.isHungry = !ready;
                            LandFarm.animalPens[r][c] = a;
                        }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }


    private static String extractValue(String content, String key) {
        if (!content.contains(key)) return null;
        int start = content.indexOf(key) + key.length();

        int endComma = content.indexOf(",", start);
        int endBrace = content.indexOf("}", start);
        int endBracket = content.indexOf("]", start);

        int end = Integer.MAX_VALUE;
        if (endComma != -1) end = Math.min(end, endComma);
        if (endBrace != -1) end = Math.min(end, endBrace);
        if (endBracket != -1) end = Math.min(end, endBracket);

        if (end == Integer.MAX_VALUE) return null;

        return content.substring(start, end).trim();
    }

    private static void parseMap(String content, String key, Map<String, Integer> targetMap) {
        if (!content.contains(key)) return;
        int start = content.indexOf(key);
        int mapStart = content.indexOf("{", start) + 1;
        int mapEnd = content.indexOf("}", mapStart);
        String section = content.substring(mapStart, mapEnd).trim();

        if (section.isEmpty()) return;

        String[] pairs = section.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                String k = kv[0].trim().replace("\"", "");
                int v = Integer.parseInt(kv[1].trim());
                targetMap.put(k, v);
            }
        }
    }

    private static String mapToJson(Map<String, Integer> map) {
        StringBuilder sb = new StringBuilder("{");
        map.forEach((k, v) -> sb.append("\"").append(k).append("\":").append(v).append(","));
        if (sb.length() > 1) sb.setLength(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
}