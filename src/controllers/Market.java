package controllers;

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
    private Label lblMoney;   // afficher le solde total
    @FXML
    private Label lblMessage; // afficher "Vous avez gagné X €"

    @FXML
    public void initialize() {
        updateUI();
    }

    private void updateUI() {
        lblMoney.setText("Monnaie : " + Stocks.money + " €");
    }

    @FXML
    public void sellPatates(ActionEvent event) {
        // Prix de la patate : 100 (défini dans ta classe Patate)
        double gain = Stocks.instance.sellPlant("Patate", 100.0);
        showResult("Patates", gain);
    }

    @FXML
    public void sellMais(ActionEvent event) {
        // Prix du maïs : 300 (défini dans ta classe Mais)
        double gain = Stocks.instance.sellPlant("Maïs", 300.0);
        showResult("Maïs", gain);
    }

    private void showResult(String name, double gain) {
        if (gain > 0) {
            lblMessage.setText("Vente de " + name + " terminée ! Gain : +" + gain + " €");
        } else {
            lblMessage.setText("Vous n'avez pas de " + name + " en stock...");
        }
        updateUI();
    }

    @FXML
    public void goToFarm(ActionEvent event) throws IOException {
        Parent farmView = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(farmView));
        stage.show();
    }
}