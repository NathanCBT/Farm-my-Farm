package controllers;

import Animals.Animal;
import Plants.Plant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class LandFarm {
    @FXML
    private Button land;
    @FXML
    public GridPane gridlands;
    public GridPane gridplantslist;

    @FXML
    private Label lblStatus;
    public static Label statusLabel;

    // aller sur la page du marché
    @FXML
    public void goToMarket(ActionEvent event) throws IOException {
        Parent marketView = FXMLLoader.load(getClass().getResource("/fxml/market.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(marketView));
        stage.show();
    }

    @FXML
    public GridPane gridAnimalsList;

    // pour enregistrer la partie
    @FXML
    public void handleSave() {
        SaveManager.saveGame();
        lblStatus.setText("Partie sauvegardée !");
    }

    @FXML private Label lblMoney;
    @FXML private Label lblPotato;
    @FXML private Label lblMaize;
    @FXML private Label lblEggs;
    @FXML private Label lblMilk;

    // Permet d'accéder à l'interface depuis Land, pour mettre a jour le stock affiché sur le bouton
    public static LandFarm instance;


    public static boolean[][] ownedLands = new boolean[10][10];
    private static boolean isFirstLaunch = true;

    public void initialize() {
        instance = this; // enregistre l'instance actuelle
        updateUI();
        statusLabel = lblStatus;
        if (isFirstLaunch) {
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 3; col++) {
                    ownedLands[row][col] = true; // On débloque les 3 premières colonnes
                }
            }
            isFirstLaunch = false;
        }
        generateLands();
        generatePlantsList();
        generateAnimalList();
    }

    public static void updateUI() {
        if (instance != null && instance.lblMoney != null) {
            instance.lblMoney.setText(String.format("%.2f", Stocks.money) + " €");
            instance.lblPotato.setText("🥔 Patates : " + Stocks.harvests.getOrDefault("Patate", 0));
            instance.lblMaize.setText("🌽 Maïs : " + Stocks.harvests.getOrDefault("Maïs", 0));
            instance.lblEggs.setText("🥚 Œufs : " + Stocks.animalProducts.getOrDefault("Oeuf", 0));
            instance.lblMilk.setText("🥛 Lait : " + Stocks.animalProducts.getOrDefault("Lait", 0));
        }
    }


    // Tableau pour stocker l'état des plantes (20x20)
    public static Plant[][] plantedSeeds = new Plant[10][10];

    public void generateLands() {
        gridlands.getChildren().clear();
        int rows = 10;
        int columns = 10;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                boolean freeAtStart = (col < 3);

                Land land = new Land(row, col);

                gridlands.add(land.getButton(), col, row);
            }
        }
    }

    // Crée une variable statique pour que Land puisse y accéder
    public static String selectedPlantType = null;

    public void generatePlantsList() {
        gridplantslist.getChildren().clear(); // On vide la liste avant de la recréer
        int count = 0;
        for (String plantName : Stocks.seeds.keySet()) {
            int qty = Stocks.seeds.get(plantName);
            Button btn = new Button(plantName + " (" + qty + ")");

            btn.setOnAction(e -> {
                selectedPlantType = plantName;
                System.out.println("Plante sélectionnée : " + selectedPlantType);
            });

            gridplantslist.add(btn, 0, count);
            count += 1;
        }
    }

    // grille dédiée aux animaux
    public static Animal[][] animalPens = new Animal[20][20];
    public static String selectedAnimalType = null;

    public void generateAnimalList() {
        gridAnimalsList.getChildren().clear();
        int count = 0;

        for (String animalName : Stocks.animals.keySet()) {
            int qty = Stocks.animals.get(animalName);
            String productName = getProductName(animalName);
            int productQty = Stocks.animalProducts.getOrDefault(productName, 0);

            Button btn = new Button(animalName + ": " + qty + "\n(" + productName + ": " + productQty + ")");
            btn.setStyle("-fx-background-color: #FFD700; -fx-text-alignment: center;");

            btn.setOnAction(e -> {
                selectedAnimalType = animalName;
                selectedPlantType = null; // On déselectionne la plante si on choisit un animal
                statusLabel.setText("Animal sélectionné : " + animalName);
            });

            gridAnimalsList.add(btn, 0, count++);
        }
    }


    private String getProductName(String animal) {
        switch (animal) {
            case "Poule": return "Oeuf";
            case "Vache": return "Lait";
            default: return "";
        }
    }
}
