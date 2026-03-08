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
            if (this.plant == null || (!this.plant.isGrowing && !this.plant.collectAuthorized)) {

                // SÉCURITÉ : Si l'utilisateur n'a rien choisi en bas, on arrête tout
                if (LandFarm.selectedPlantType == null) {
                    LandFarm.statusLabel.setText("Sélectionnez une plante d'abord !");
                    return; // On sort de la fonction immédiatement
                }

                // Si on est ici, c'est que selectedPlantType n'est pas null
                if (LandFarm.selectedPlantType.equals("Patate")) {
                    this.plant = new Patate();
                } else if (LandFarm.selectedPlantType.equals("Maïs")) {
                    this.plant = new Mais();
                }

                // On lance la croissance
                this.plant.growthDuration(this.land);
                System.out.println(this.plant.name + " planté !");
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
