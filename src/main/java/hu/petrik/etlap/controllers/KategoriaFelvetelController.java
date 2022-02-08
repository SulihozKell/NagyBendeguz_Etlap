package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.EtlapDb;
import hu.petrik.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

public class KategoriaFelvetelController extends Controller {
    @FXML
    private TextField inputNev;

    public void onKategoriaFelvetelButtonClick(ActionEvent actionEvent) {
        String nev = inputNev.getText().trim();
        if (nev.isEmpty()) {
            alert("A név megadása kötelező!");
            return;
        }
        try {
            EtlapDb db = new EtlapDb();
            List<Kategoria> kategoriaList = db.getKategoria();
            for (Kategoria kategoria : kategoriaList) {
                if (kategoria.getNev().equals(nev.toLowerCase())) {
                    alert("Az adott névvel már létezik kategória!");
                    return;
                }
            }
            if (!inputNev.equals("")) {
                EtlapDb etlapDb = new EtlapDb();
                int success = etlapDb.newKategoria(nev);
                if (success == 1) {
                    alert("Az új kategória felvétele sikeres!");
                    inputNev.setText("");
                }
                else {
                    alert("Az új kategória felvétele sikertelen!");
                }
            }
        }
        catch (Exception e) {
            errorAlert(e);
        }
    }
}
