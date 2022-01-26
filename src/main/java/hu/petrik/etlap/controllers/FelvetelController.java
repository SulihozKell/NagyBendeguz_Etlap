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
        /*if (nev.isEmpty() || leiras.isEmpty()) {

        }*/
        ar = inputAr.getValue();

        try {
            EtlapDb db = new EtlapDb();
            int success =db.etlapFelvetele(nev, leiras, ar, kategoria);
            /*if(success == 1) {

            }
            else {

            }*/
        }
        catch (SQLException e) {
            System.out.println(e);
        }

    }
}
