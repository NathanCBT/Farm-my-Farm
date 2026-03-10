package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import Plants.Plant;
import Plants.Mais;
import Plants.Patate;

public class Land {
    @FXML
    public Button land;

    public Plant plant;
    public boolean isOwned;
    public static final int LAND_PRICE = 350;
    public int row, col;

    public Land(int row, int col) {
        this.row = row;
        this.col = col;
        this.land = new Button();
        // rehercher l'état sauvegardé dans le tableau statique
        this.isOwned = LandFarm.ownedLands[row][col];
        this.land.setPrefSize(80, 80); // taille optionnelle pour stabiliser la grille

        // on récupère la plante depuis la mémoire statique
        this.plant = LandFarm.plantedSeeds[row][col];

        updateAppearance();

        // si une plante était déjà là on met à jour l'emoji sur le bouton
        if (this.plant != null) {
            if (this.plant.collectAuthorized) {
                land.setText(this.plant.emojiList.get(2)); // Prête
            } else {
                land.setText(this.plant.emojiList.get(1)); // En pousse
                // Optionnel : tu pourrais relancer la Timeline ici si tu veux qu'elle finisse de pousser
            }
        }
        addPlant();
    }

    // met à jour le bouton visuellement selon l'état isOwned
    private void updateAppearance() {
        if (isOwned) {
            land.setText("🆕");
            land.setStyle("-fx-background-color: #90EE90; -fx-border-color: #555;");
        } else {
            land.setText("🔒\n350€");
            land.setStyle("-fx-background-color: #FFB6C1; -fx-border-color: #555;");
        }
    }

    public void addPlant() {
        land.setOnAction(e -> {
            // achat de parcelles de terre
            if (!isOwned) {
                if (Stocks.money >= LAND_PRICE) {
                    Stocks.money -= LAND_PRICE;
                    this.isOwned = true;

                    // enregistre l'achat dans la mémoire statique
                    LandFarm.ownedLands[this.row][this.col] = true;

                    updateAppearance();
                    LandFarm.statusLabel.setText("Parcelle achetée !");
                } else {
                    LandFarm.statusLabel.setText("Pas assez d'argent !");
                }
                return;
            }

            if (this.plant == null || (!this.plant.isGrowing && !this.plant.collectAuthorized)) {
                if (LandFarm.selectedPlantType == null) {
                    LandFarm.statusLabel.setText("Sélectionnez une plante d'abord !");
                    return;
                }

                if (LandFarm.selectedPlantType.equals("Patate")) {
                    this.plant = new Patate();
                } else if (LandFarm.selectedPlantType.equals("Maïs")) {
                    this.plant = new Mais();
                }
                // on enregistre la plante dans le tableau statique
                LandFarm.plantedSeeds[this.row][this.col] = this.plant;
                this.plant.growthDuration(this.land);
                LandFarm.statusLabel.setText("🌱 " + this.plant.name + " planté !");
            }

            else if (this.plant.collectAuthorized) {
                Stocks.instance.add(this.plant.name, 1);
                // on retire la plante de la mémoire statique
                LandFarm.plantedSeeds[this.row][this.col] = null;
                land.setText("🆕");
                this.plant.collectAuthorized = false;
                this.plant = null;

                if (LandFarm.instance != null) {
                    LandFarm.instance.generatePlantsList();
                }
                LandFarm.statusLabel.setText("Récolte effectuée !");
            }
        });
    }

    public Button getButton() {
        return land;
    }
}