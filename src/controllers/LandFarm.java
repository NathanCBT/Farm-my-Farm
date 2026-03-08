package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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

    // Permet d'accéder à l'interface depuis Land, pour mettre a jour le stock affiché sur le bouton
    public static LandFarm instance;

    public void initialize() {
        instance = this; // enregistre l'instance actuelle
        statusLabel = lblStatus;
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
    public static String selectedPlantType = null;

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
