package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.EtlapDb;
import hu.petrik.etlap.Kategoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class KategoriaController extends Controller {
    @FXML
    private TableView<Kategoria> kategoriaTable;
    @FXML
    private TableColumn<Kategoria, String> kategoriaNev;
    private EtlapDb db;

    public void initialize() {
        kategoriaNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        try {
            db = new EtlapDb();
            tablazatKategoriaFeltolt();
        }
        catch (SQLException e) {
            errorAlert(e);
        }
    }

    private void tablazatKategoriaFeltolt() {
        try {
            List<Kategoria> kategoriaList = db.getKategoria();
            kategoriaTable.getItems().clear();
            for (Kategoria kategoria: kategoriaList) {
                kategoriaTable.getItems().add(kategoria);
            }
        }
        catch (SQLException e) {
            errorAlert(e);
        }
    }

    public void onKategoriaHozzaadasButton(ActionEvent actionEvent) {
        try {
            Controller kategoriaFelvetel = newWindow("kategoria_felvetel-view.fxml", "Kategória hozzáadása", 300, 100);
            kategoriaFelvetel.getStage().setOnCloseRequest(event -> tablazatKategoriaFeltolt());
            kategoriaFelvetel.getStage().show();
        }
        catch (Exception e ) {
            errorAlert(e);
        }
    }

    public void onKategoriaTorlesButton(ActionEvent actionEvent) {
        if (kategoriaTable.getSelectionModel().getSelectedItem() == null) {
            alert("A törléshez előbb ki kell választani egy elemet a táblázatból.");
            return;
        }
        Kategoria deleteKategoria = kategoriaTable.getSelectionModel().getSelectedItem();
        if (!confirm("Biztosan törölni szeretné a az alábbi kategóriát:\n" + deleteKategoria.getNev())) {
            return;
        }
        try {
            db.deleteKategoria(deleteKategoria.getId());
            tablazatKategoriaFeltolt();
        }
        catch (SQLException e) {
            errorAlert(e);
        }
    }
}
