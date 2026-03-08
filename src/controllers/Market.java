package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class Market {

    @FXML
    public void goToFarm(ActionEvent event) throws IOException {
        Parent farmView = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(farmView));
        stage.show();
    }

    // Tu pourras ajouter ici la logique pour vendre (modifier Stocks.stocks)
}