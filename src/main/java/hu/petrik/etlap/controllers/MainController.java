package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.Etlap;
import hu.petrik.etlap.EtlapDb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainController extends Controller {
    @FXML
    private Label kivalasztottElem;
    @FXML
    private TableView<Etlap> etlapTable;
    @FXML
    private TableColumn<Etlap, String> nevOszlop;
    @FXML
    private TableColumn<Etlap, Integer> arOszlop;
    @FXML
    private TableColumn<Etlap, String> kategoriaOszlop;
    private EtlapDb db;

    public void initialize() {
        nevOszlop.setCellValueFactory(new PropertyValueFactory<>("nev"));
        arOszlop.setCellValueFactory(new PropertyValueFactory<>("ar"));
        kategoriaOszlop.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        try {
            db = new EtlapDb();
            tablazatEtlapFeltolt();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void tablazatEtlapFeltolt() {
        try {
            List<Etlap> etlapLista = db.getEtlap();
            etlapTable.getItems().clear();
            for (Etlap etlapItem : etlapLista) {
                etlapTable.getItems().add(etlapItem);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectedItem(MouseEvent mouseEvent) {
        // Javít
        kivalasztottElem.setText(etlapTable.getSelectionModel().getSelectedItem().toString());
    }

    public void onFelvetelButtonClick(ActionEvent actionEvent) {
        try {
            Controller felvesz = ujAblak("felvetel-view.fxml", "Új étel felvétele", 540, 400);
            felvesz.getStage().setOnCloseRequest(event -> tablazatEtlapFeltolt());
            felvesz.getStage().show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}