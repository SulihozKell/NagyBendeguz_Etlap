package hu.petrik.etlap.controllers;

import hu.petrik.etlap.Controller;
import hu.petrik.etlap.Etlap;
import hu.petrik.etlap.EtlapDb;
import hu.petrik.etlap.Kategoria;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainController extends Controller {
    @FXML
    public Spinner<Integer> ftSpinner;
    @FXML
    private Spinner<Integer> szazalekSpinner;
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
    public ChoiceBox<String> kereses;

    public void initialize() {
        nevOszlop.setCellValueFactory(new PropertyValueFactory<>("nev"));
        arOszlop.setCellValueFactory(new PropertyValueFactory<>("ar"));
        kategoriaOszlop.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        try {
            db = new EtlapDb();
            tablazatEtlapFeltolt();
            List<Kategoria> kategoriak = db.getKategoria();
            for (Kategoria kategoria: kategoriak) {
                kereses.getItems().add(kategoria.getNev());
            }
        }
        catch (SQLException e) {
            errorAlert(e);
        }
        kereses.getItems().add("Mindegyik");
        keresesKategoria();
    }

    private void tablazatEtlapFeltolt() {
        try {
            List<Etlap> etlapLista = db.getEtlap();
            etlapTable.getItems().clear();
            for (Etlap etlapItem : etlapLista) {
                etlapTable.getItems().add(etlapItem);
            }
        }
        catch (Exception e) {
            errorAlert(e);
        }
    }

    public void selectedItem(MouseEvent mouseEvent) {
        if (etlapTable.getSelectionModel().getSelectedItem() != null) {
            Etlap kivalasztott = etlapTable.getSelectionModel().getSelectedItem();
            kivalasztottElem.setText(kivalasztott.getLeiras());
        }
    }

    public void onFelvetelButtonClick(ActionEvent actionEvent) {
        try {
            Controller felvesz = newWindow("felvetel-view.fxml", "Új étel felvétele", 540, 400);
            felvesz.getStage().setOnCloseRequest(event -> tablazatEtlapFeltolt());
            felvesz.getStage().show();
        }
        catch (IOException e) {
            errorAlert(e);
        }
    }

    public void onTorlesButtonClick(ActionEvent actionEvent) {
        int selectedIndex = etlapTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A törléshez előbb ki kell választani egy elemet a táblázatból.");
            return;
        }
        Etlap deleteEtlap = etlapTable.getSelectionModel().getSelectedItem();
        if (!confirm("Biztosan törölni szeretné a az alábbi ételt:\n" + deleteEtlap.getNev())) {
            return;
        }
        try {
            db.deleteEtlap(deleteEtlap.getId());
            tablazatEtlapFeltolt();
        }
        catch (SQLException e) {
            errorAlert(e);
        }
    }

    public void onSzazalekosEmelesButtonClick(ActionEvent actionEvent) {
        int szazalekEmeles = 0;
        try {
            szazalekEmeles = szazalekSpinner.getValue();
        }
        catch (Exception e) {
            alert("Az ár csak szám lehet!");
            return;
        }
        if (szazalekEmeles < 5 || szazalekEmeles > 50) {
            alert("Az ár százalékos emelése csak 5% és 50% között lehet!");
            return;
        }
        if (etlapTable.getSelectionModel().getSelectedItem() == null) {
            if (!confirm("Biztosan szeretné emelni az összes étel árát?")) {
                return;
            }
            try {
                db.szazalekEmelesEtlapOsszes((szazalekEmeles));
                tablazatEtlapFeltolt();
            }
            catch (SQLException e) {
                errorAlert(e);
            }
        }
        else {
            if (!confirm("Biztosan szeretné emelni az: " + etlapTable.getSelectionModel().getSelectedItem().getNev() + " árát?")) {
                return;
            }
            try {
                db.szazalekEmelesEtlapKivalasztott(etlapTable.getSelectionModel().getSelectedItem().getId(), szazalekEmeles);
                tablazatEtlapFeltolt();
            }
            catch (SQLException e) {
                errorAlert(e);
            }
        }
    }

    public void onForintosEmelesButtonClick(ActionEvent actionEvent) {
        int ftEmeles = 0;
        try {
            ftEmeles = ftSpinner.getValue();
        }
        catch (Exception e) {
            alert("Az ár csak szám lehet!");
            return;
        }
        if (ftEmeles < 50 || ftEmeles > 3000) {
            alert("Az ár forintos emelése csak 50Ft és 3000Ft között lehet!");
            return;
        }
        if (etlapTable.getSelectionModel().getSelectedItem() == null) {
            if (!confirm("Biztosan szeretné emelni az összes étel árát?")) {
                return;
            }
            try {
                db.ftEmelesEtlapOsszes((ftEmeles));
                tablazatEtlapFeltolt();
            }
            catch (SQLException e) {
                errorAlert(e);
            }
        }
        else {
            if (!confirm("Biztosan szeretné emelni az: " + etlapTable.getSelectionModel().getSelectedItem().getNev() + " árát?")) {
                return;
            }
            try {
                db.ftEmelesEtlapKivalasztott(etlapTable.getSelectionModel().getSelectedItem().getId(), ftEmeles);
                tablazatEtlapFeltolt();
            }
            catch (SQLException e) {
                errorAlert(e);
            }
        }
    }

    public void onKategoriaButtonClick(ActionEvent actionEvent) {
        try {
            Controller kategoria = newWindow("kategoria-view.fxml", "Kategória", 540, 400);
            kategoria.getStage().show();
        }
        catch (Exception e) {
            errorAlert(e);
        }
    }

    public void keresesKategoria() {
        kereses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String regi, String uj) {
                try {
                    if (!uj.equals("Mindegyik")) {
                        List<Etlap> keresettLista = db.keresesEtlap(uj);
                        etlapTable.getItems().clear();
                        for (Etlap etlap: keresettLista) {
                            etlapTable.getItems().add(etlap);
                        }
                    }
                    else {
                        tablazatEtlapFeltolt();
                    }
                }
                catch (SQLException e) {
                    errorAlert(e);
                }
            }
        });
    }
}