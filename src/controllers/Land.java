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

    public void addPlant() {
        land.setOnAction(e -> {
            // Si aucune plante n'est en cours de pousse ou prête à être récoltée
            if (this.plant == null || (!this.plant.isGrowing && !this.plant.collectAuthorized)) {

                // On récupère le type sélectionné au moment du CLIC
                String currentType = LandFarm.selectedPlantType;

                if (currentType.equals("Patate")) {
                    this.plant = new Patate();
                } else {
                    this.plant = new Mais();
                }

                this.plant.growthDuration(this.land);
            }
            // Si la plante est prête à être récoltée
            else if (this.plant.collectAuthorized) {
                Stocks.instance.add(this.plant.name, 1);
                land.setText("🆕");
                this.plant.collectAuthorized = false;
                this.plant = null; // On réinitialise la case

                // on demande à LandFarm de rafraîchir les boutons
                if (LandFarm.instance != null) {
                    LandFarm.instance.generatePlantsList();
                }

                System.out.println("Récolte effectuée ! Nouveau stock : " + Stocks.stocks);
            }
        });
    }

    public Button getButton() {
        return land;
    }
}
