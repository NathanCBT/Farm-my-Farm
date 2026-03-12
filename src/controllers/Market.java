package controllers;

import Plants.Mais;
import Plants.Patate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class Market {

    @FXML
    private Label lblMoney;
    @FXML
    private Label lblMessage;

    @FXML
    public void initialize() {
        updateUI();
    }

    private void updateUI() {
        lblMoney.setText("Monnaie : " + Stocks.money + " €");
    }

    @FXML
    public void buyPatateSeed() {
        Patate p = new Patate();
        if (Stocks.money >= p.buyMoney) {
            Stocks.money -= p.buyMoney;
            Stocks.instance.addSeeds("Patate", 1);
            lblMessage.setText("Achat : 1 Graine de Patate (-" + p.buyMoney + "€)");
            updateUI();
            LandFarm.updateUI();
        } else {
            lblMessage.setText("Pas assez d'argent !");
        }
    }

    @FXML
    public void buyMaisSeed() {
        Mais m = new Mais();
        if (Stocks.money >= m.buyMoney) {
            Stocks.money -= m.buyMoney;
            Stocks.instance.addSeeds("Maïs", 1);
            lblMessage.setText("Achat : 1 Graine de Maïs (-" + m.buyMoney + "€)");
            updateUI();
            LandFarm.updateUI();
        } else {
            lblMessage.setText("Pas assez d'argent !");
        }
    }

    @FXML
    public void sellPatates() {
        Patate p = new Patate();
        int qty = Stocks.harvests.getOrDefault("Patate", 0);

        if (qty > 0) {
            double gain = Stocks.instance.sellHarvest("Patate", p.sellMoney);
            lblMessage.setText("Vente de " + qty + " Patates pour " + gain + "€");
            updateUI();
            LandFarm.updateUI();
        } else {
            lblMessage.setText("Vous n'avez pas de patates à vendre !");
        }
    }

    @FXML
    public void sellMais() {
        Mais m = new Mais();
        int qty = Stocks.harvests.getOrDefault("Maïs", 0);

        if (qty > 0) {
            double gain = Stocks.instance.sellHarvest("Maïs", m.sellMoney);
            lblMessage.setText("Vente de " + qty + " Maïs pour " + gain + "€");
            updateUI();
            LandFarm.updateUI();
        } else {
            lblMessage.setText("Vous n'avez pas de maïs à vendre !");
        }
    }

    @FXML
    public void buyChicken() {
        Animals.Poule c = new Animals.Poule();
        if (Stocks.money >= c.buyMoney) {
            Stocks.money -= c.buyMoney;
            Stocks.animals.put("Poule", Stocks.animals.get("Poule") + 1);
            lblMessage.setText("Achat : 1 Poule (-" + c.buyMoney + "€)");
            updateUI();
            LandFarm.updateUI();
        } else {
            lblMessage.setText("Pas assez d'argent pour une Poule !");
        }
    }

    @FXML
    public void sellEggs() {
        int qty = Stocks.animalProducts.get("Oeuf");
        if (qty > 0) {
            double price = 15.0;
            double total = qty * price;
            Stocks.money += total;
            Stocks.animalProducts.put("Oeuf", 0);
            lblMessage.setText("Vente : " + qty + " Oeufs pour " + total + "€");
            updateUI();
            LandFarm.updateUI();
        } else {
            lblMessage.setText("Vous n'avez pas d'oeufs !");
        }
    }

    @FXML
    public void buyCow() {
        Animals.Vache v = new Animals.Vache();
        if (Stocks.money >= v.buyMoney) {
            Stocks.money -= v.buyMoney;
            Stocks.animals.put("Vache", Stocks.animals.get("Vache") + 1);
            lblMessage.setText("Achat : 1 Vache (-" + v.buyMoney + "€)");
            updateUI();
            LandFarm.updateUI();
        } else {
            lblMessage.setText("Pas assez d'argent pour une Vache !");
        }
    }

    @FXML
    public void sellMilks() {
        int qty = Stocks.animalProducts.get("Lait");
        if (qty > 0) {
            double price = 150.0;
            double total = qty * price;
            Stocks.money += total;
            Stocks.animalProducts.put("Lait", 0);
            lblMessage.setText("Vente : " + qty + " Lait pour " + total + "€");
            updateUI();
            LandFarm.updateUI();
        } else {
            lblMessage.setText("Vous n'avez pas de lait !");
        }
    }

    @FXML
    public void goToFarm(ActionEvent event) throws IOException {
        Parent farmView = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(farmView));
        stage.show();
    }
}