package hu.petrik.etlap;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class MainController {
    @FXML
    private TableView<Etlap> etlapTable;
    @FXML
    private TableColumn<Etlap, String> nevOszlop;
    @FXML
    private TableColumn<Etlap, String> leirasOszlop;
    @FXML
    private TableColumn<Etlap, Integer> arOszlop;
    @FXML
    private TableColumn<Etlap, String> kategoriaOszlop;
    private EtlapDb db;

    public void initialize() {
        nevOszlop.setCellValueFactory(new PropertyValueFactory<>("nev"));
        leirasOszlop.setCellValueFactory(new PropertyValueFactory<>("leiras"));
        arOszlop.setCellValueFactory(new PropertyValueFactory<>("ar"));
        kategoriaOszlop.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        tablazatEtlapFeltolt();
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
}