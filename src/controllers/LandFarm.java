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

    public void initialize() {
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

    public void generatePlantsList() {
        int count = 0;
        for (String plantName : Stocks.stocks.keySet()) {
            int qty = Stocks.stocks.get(plantName);
            Button btn = new Button(plantName + " " + qty); // texte du bouton = nom de la plante

            btn.setOnAction(e -> {
                System.out.println("selectionner");
            });

            gridplantslist.add(btn, 0, count);
            count += 1;
        }
    }
}
