package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import Plants.Plant;
import Plants.Mais;
import Plants.Patate;
import Animals.Animal;
import Animals.Poule;
import Animals.Vache;

public class Land {
    @FXML
    public Button land;
    public Plant plant;
    public Animal animal;
    public boolean isOwned;
    public static final int LAND_PRICE = 150;
    public int row, col;

    public Land(int row, int col) {
        this.row = row;
        this.col = col;
        this.land = new Button();
        // rehercher l'état sauvegardé dans le tableau statique
        this.isOwned = LandFarm.ownedLands[row][col];
        this.land.setPrefSize(65, 65);
        this.land.setMinSize(65, 65);
        this.land.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");
        this.land.setWrapText(true);
        // on récupère la plante depuis la mémoire statique
        this.plant = LandFarm.plantedSeeds[row][col];
        this.animal = LandFarm.animalPens[row][col];

        updateAppearance();

        // si une plante était déjà là on met à jour l'emoji sur le bouton
        if (this.plant != null) {
            if (this.plant.collectAuthorized) {
                land.setText(this.plant.emojiList.get(2)); // prête
            } else {
                land.setText(this.plant.emojiList.get(1)); // en pousse
                this.plant.growthDuration(this.land);
            }
        }

        if (this.animal != null) {
            if (this.animal.canCollect) {
                land.setText(this.animal.emojiProduct);
            }
            else {
                land.setText(this.animal.emojiAnimal);
                this.animal.produce(this.land);
            }
        }
        addPlant();
    }

    // met à jour le bouton visuellement selon l'état isOwned
    private void updateAppearance() {
        if (isOwned) {
            land.setText("🆕");
            land.setStyle("-fx-background-color: #b46c3c; -fx-border-color: #555;");
        } else {
            land.setText("🔒\n350€");
            land.setStyle("-fx-background-color: #b46c3c; -fx-border-color: #555;");
        }
    }

    public void addPlant() {
        land.setOnAction(e -> {
            // achat de parcelles de terre
            if (!isOwned) {
                if (Stocks.money >= LAND_PRICE) {
                    Stocks.money -= LAND_PRICE;
                    this.isOwned = true;
                    LandFarm.ownedLands[this.row][this.col] = true;
                    updateAppearance();
                    LandFarm.statusLabel.setText("Parcelle achetée !");
                    LandFarm.updateUI();
                } else {
                    LandFarm.statusLabel.setText("Pas assez d'argent !");
                }
                return;
            }

            // gérer l'animal
            if (this.animal != null) {
                // la récolte de l'animal
                if (this.animal.canCollect) {
                    int currentQty = Stocks.animalProducts.getOrDefault(this.animal.productName, 0);
                    Stocks.animalProducts.put(this.animal.productName, currentQty + 1);

                    this.animal.canCollect = false;
                    this.animal.isHungry = true; // L'animal a de nouveau faim après récolte
                    this.land.setText(this.animal.emojiAnimal + " 😋?");

                    if (LandFarm.instance != null) LandFarm.instance.generateAnimalList();
                    LandFarm.statusLabel.setText("Récolte de " + this.animal.productName + " effectuée !");
                    LandFarm.updateUI();
                }
                // nourrir l'animal
                else if (this.animal.isHungry) {
                    String food = this.animal.foodNeeded;
                    int stockRecolte = Stocks.harvests.getOrDefault(food, 0);

                    if (stockRecolte > 0) {
                        Stocks.harvests.put(food, stockRecolte - 1);
                        this.animal.isHungry = false;
                        this.animal.produce(this.land);

                        if (LandFarm.instance != null) LandFarm.instance.generatePlantsList();
                        LandFarm.statusLabel.setText("Vous avez nourri la " + this.animal.name + " avec 1 " + food);
                        LandFarm.updateUI();
                    } else {
                        LandFarm.statusLabel.setText("Il vous faut 1 " + food + " pour nourrir cet animal !");
                    }
                } else {
                    LandFarm.statusLabel.setText("L'animal est en train de produire...");
                }
                return; // return car une case avec un animal ne peut pas recevoir de graine
            }

            // gérer une plante si il y en a une
            if (this.plant != null) {
                if (this.plant.collectAuthorized) {
                    Stocks.instance.addHarvest(this.plant.name, 1);
                    LandFarm.plantedSeeds[this.row][this.col] = null;
                    this.plant = null;
                    this.land.setText("🆕");

                    if (LandFarm.instance != null) LandFarm.instance.generatePlantsList();
                    LandFarm.statusLabel.setText("Récolte de la plante effectuée !");
                    LandFarm.updateUI();
                } else {
                    LandFarm.statusLabel.setText("La plante n'est pas encore prête.");
                }
                return;
            }

            // poser un animal
            if (LandFarm.selectedAnimalType != null) {
                String type = LandFarm.selectedAnimalType;
                int nbAnimaux = Stocks.animals.getOrDefault(type, 0);

                if (nbAnimaux > 0) {
                    Stocks.animals.put(type, nbAnimaux - 1);
                    if (type.equals("Poule")) this.animal = new Poule();
                    else if (type.equals("Vache")) this.animal = new Vache();

                    LandFarm.animalPens[this.row][this.col] = this.animal;
                    this.land.setText(this.animal.emojiAnimal + " 😋?");

                    if (LandFarm.instance != null) LandFarm.instance.generateAnimalList();
                    LandFarm.statusLabel.setText(type + " installé !");
                    LandFarm.updateUI();
                    LandFarm.selectedAnimalType = null; // Optionnel : déselectionner après pose
                } else {
                    LandFarm.statusLabel.setText("Vous n'avez pas de " + type + " en stock !");
                }
            }
            // planter une graine
            else if (LandFarm.selectedPlantType != null) {
                String type = LandFarm.selectedPlantType;
                int nbGraines = Stocks.seeds.getOrDefault(type, 0);

                if (nbGraines > 0) {
                    Stocks.seeds.put(type, nbGraines - 1);
                    if (type.equals("Patate")) this.plant = new Patate();
                    else if (type.equals("Maïs")) this.plant = new Mais();

                    LandFarm.plantedSeeds[this.row][this.col] = this.plant;
                    this.plant.growthDuration(this.land);

                    if (LandFarm.instance != null) LandFarm.instance.generatePlantsList();
                    LandFarm.statusLabel.setText(this.plant.name + " planté !");
                    LandFarm.updateUI();
                    /*LandFarm.selectedPlantType = null;*/
                } else {
                    LandFarm.statusLabel.setText("Plus de graines de " + type + " !");
                }
            } else {
                LandFarm.statusLabel.setText("Sélectionnez d'abord une graine ou un animal !");
            }
        });
    }

    public Button getButton() {
        return land;
    }
}