package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.EtlapDb;
import hu.petrik.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

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
            List<Kategoria> kategoriaList = db.getKategoria();
            int kategoria_id = 0;
            for (Kategoria kategoria : kategoriaList) {
                if (kategoria.getNev().equals(inputKategoria.getValue())) {
                    kategoria_id = kategoria.getId();
                }
            }
            int success = db.newEtlap(nev, leiras, ar, kategoria_id);
            if (success == 1) {
                alert("Az új étel felvétele sikeres.");
                inputNev.setText("");
                inputLeiras.setText("");
                inputAr.getValueFactory().setValue(1);
                inputKategoria.setValue("főétel");
            }
            else {
                alert("Az új étel felvétele sikertelen.");
            }
        }
        catch (SQLException e) {
            errorAlert(e);
        }
    }
}
