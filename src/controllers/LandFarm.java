package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class LandFarm {
    @FXML
    private Button land;
    @FXML
    public GridPane gridlands;
    public GridPane gridplantslist;

    // Permet d'accéder à l'interface depuis Land, pour mettre a jour le stock affiché sur le bouton
    public static LandFarm instance;

    public void initialize() {
        instance = this; // enregistre l'instance actuelle
        generateLands();
        generatePlantsList();
    }

    public void generateLands() {
        int rows = 20;
        int columns = 20;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Land land = new Land("Maïs");

                gridlands.setHalignment(land.getButton(), javafx.geometry.HPos.CENTER);
                gridlands.setValignment(land.getButton(), javafx.geometry.VPos.CENTER);
                gridlands.setAlignment(Pos.CENTER);

                gridlands.add(land.getButton(), col, row);
            }
        }
    }

    // Crée une variable statique pour que Land puisse y accéder
    public static String selectedPlantType = "Maïs";

    public void generatePlantsList() {
        gridplantslist.getChildren().clear(); // On vide la liste avant de la recréer
        int count = 0;
        for (String plantName : Stocks.stocks.keySet()) {
            int qty = Stocks.stocks.get(plantName);
            Button btn = new Button(plantName + " (" + qty + ")");

            btn.setOnAction(e -> {
                selectedPlantType = plantName; // On change la plante sélectionnée globalement
                System.out.println("Plante sélectionnée : " + selectedPlantType);
            });

            gridplantslist.add(btn, 0, count);
            count += 1;
        }
    }
}
