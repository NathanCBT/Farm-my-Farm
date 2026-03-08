package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import Plants.Plant;
import Plants.Mais;
import Plants.Patate;
import javafx.scene.layout.GridPane;

public class Land {
    @FXML
    public Button land;

    @FXML
    private GridPane gridModalChoicePlants;

    public Plant plant;
    public String typePlant;

    public Land(String typePlant) {
        this.land = new Button("🆕");
        this.typePlant = typePlant;

        addPlant();
    }

    public void addPlant () {
        land.setOnAction(e -> {
             if (typePlant.equals("Patate")) {
                this.plant = new Patate();
            }

            if (typePlant.equals("Maïs")) {
                this.plant = new Mais();
            }

            System.out.println(plant.name);
            System.out.println(this.plant.collectAuthorized);

            if (this.plant.collectAuthorized) {
                Stocks.instance.add(this.plant.name, 1);
                land.setText("🆕");
                System.out.println(Stocks.stocks);
                this.plant.collectAuthorized = false;
            } else {
                this.plant.growthDuration(this.land);
            }
        });
    }

    public Button getButton() {
        return land;
    }
}
