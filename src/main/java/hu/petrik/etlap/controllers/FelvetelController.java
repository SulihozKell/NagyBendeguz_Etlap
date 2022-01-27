package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.EtlapDb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class FelvetelController extends Controller {
    @FXML
    public TextField inputNev;
    @FXML
    public TextField inputLeiras;
    @FXML
    public Spinner<Integer> inputAr;
    @FXML
    public ChoiceBox<String> inputKategoria;

    @FXML
    public void onFelvetelButtonClick(ActionEvent actionEvent) {
        String nev = inputNev.getText().trim();
        String leiras = inputLeiras.getText().trim();
        int ar = 0;
        String kategoria = inputKategoria.getSelectionModel().getSelectedItem();
        try {
            ar = inputAr.getValue();
        }
        catch (NullPointerException e) {
            alert("Az ár megadása kötelező!");
            return;
        }
        catch (Exception e) {
            alert("Az értéknek 0 és 99999 között kell lennie!");
            return;
        }
        if (nev.isEmpty() || leiras.isEmpty()) {
            alert("Nem hagyható üresen egy mező sem.");
            return;
        }
        if (ar < 1 || ar > 99999) {
            alert("Az ár csak 1 és 99999 között lehet!");
            return;
        }
        try {
            EtlapDb db = new EtlapDb();
            int success = db.newEtlap(nev, leiras, ar, kategoria);
            if (success == 1) {
                alert("Az új étel felvétele sikeres.");
            }
            else {
                alert("Az új étel felvétele sikertelen.");
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }
}
