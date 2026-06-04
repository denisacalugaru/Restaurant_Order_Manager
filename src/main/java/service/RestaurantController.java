package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Masa;
import repository.MasaRepository;
import java.util.ArrayList;
import java.util.List;

public class RestaurantController {

    @FXML private ListView<String> menuListView;
    @FXML private TextField txtNumarMasa;
    @FXML private TextField txtCapacitate;

    private MasaRepository masaRepository = MasaRepository.getInstance();
    private List<String> comandaCurenta = new ArrayList<>();
    private double totalDePlata = 0.0;

    @FXML
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList(
                "Ciorba Radauteana - 18.0 RON",
                "Ciorba de Burta - 19.0 RON",
                "Omleta Casei - 22.0 RON",
                "Salata Vegana - 28.0 RON",
                "Salata Caesar - 32.0 RON",
                "Paste cu Pui - 35.0 RON",
                "Paste Carbonara - 38.0 RON",
                "Burger Vita cu Cartofi - 42.0 RON",
                "Snitel de Pui - 25.0 RON",
                "Tochitura cu Mamaliga - 45.0 RON",
                "Cartofi Prajiti - 10.0 RON",
                "Legume la Gratar - 14.0 RON",
                "Clatite cu Ciocolata - 16.0 RON",
                "Papanasi cu Smantana - 24.0 RON",
                "Cafea Espresso - 10.0 RON",
                "Cappuccino - 13.0 RON",
                "Apa Minerala - 8.0 RON",
                "Apa Plata - 8.0 RON",
                "Limonada Clasica - 15.0 RON",
                "Coca Cola - 9.0 RON",
                "Bere Blonda - 12.0 RON",
                "Vin Rosu - 25.0 RON"
        );
        menuListView.setItems(items);
    }

    @FXML
    public void handleGolesteFormular(javafx.event.ActionEvent event) { 
        String produsSelectat = menuListView.getSelectionModel().getSelectedItem();

        if (produsSelectat == null) {
            afiseazaAlerta("Atentie", "Te rog selectaza un produs din meniu pentru a-l adauga!", Alert.AlertType.WARNING);
            return;
        }

        comandaCurenta.add(produsSelectat);

        try {
            String pretString = produsSelectat.split("-")[1].replaceAll("[^0-9.]", "").trim();
            totalDePlata += Double.parseDouble(pretString);

            afiseazaAlerta("Produs Adaugat",
                    "Ai adaugat " + produsSelectat.split("-")[0].trim() + " la comanda.\n" +
                            "Total actual: " + String.format("%.2f", totalDePlata) + " RON",
                    Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        menuListView.getSelectionModel().clearSelection();
    }

    @FXML
    protected void onSaveButtonClick() {
        if (txtNumarMasa.getText().isEmpty() || txtCapacitate.getText().isEmpty()) {
            afiseazaAlerta("Date lipsa", "Introdu numarul mesei si capacitatea inainte de a salva!", Alert.AlertType.ERROR);
            return;
        }

        if (comandaCurenta.isEmpty()) {
            afiseazaAlerta("Comanda goala", "Selecteaza mai intai produsele din meniu si adauga-le din bara de sus!", Alert.AlertType.WARNING);
            return;
        }

        try {
            int numar = Integer.parseInt(txtNumarMasa.getText());
            int capacitate = Integer.parseInt(txtCapacitate.getText());

            // Verificam daca masa exista deja in structura fixa din PostgreSQL
            Masa masaExistenta = masaRepository.read(numar);
            if (masaExistenta != null) {
                // Daca masa exista, ii modificam doar starea in Ocupata (fara setCapacitate)
                masaExistenta.setStatus("Ocupata");
                masaRepository.update(masaExistenta);
            } else {
                // In cazul in care se introduce un numar nou (peste 10), se face crearea fizica
                Masa masaNoua = new Masa(numar, capacitate);
                masaNoua.setStatus("Ocupata");
                masaRepository.create(masaNoua);
            }

            StringBuilder rezumat = new StringBuilder();
            rezumat.append("COMANDA TRIMISA IN BAZA DE DATE\n");
            rezumat.append("MASA: ").append(numar).append(" (").append(capacitate).append(" locuri)\n");
            rezumat.append("--------------------------------\n");
            for (String p : comandaCurenta) {
                rezumat.append("- ").append(p).append("\n");
            }
            rezumat.append("--------------------------------\n");
            rezumat.append("TOTAL BON FISCAL: ").append(String.format("%.2f", totalDePlata)).append(" RON");

            afiseazaAlerta("Succes - Comanda Salvata", rezumat.toString(), Alert.AlertType.INFORMATION);

            txtNumarMasa.clear();
            txtCapacitate.clear();
            comandaCurenta.clear();
            totalDePlata = 0.0;

        } catch (NumberFormatException e) {
            afiseazaAlerta("Eroare", "Te rog introdu cifre valide pentru masa si capacitate!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleDeleteMasa(javafx.event.ActionEvent event) {
        if (txtNumarMasa.getText().isEmpty()) {
            afiseazaAlerta("Eroare", "Introdu numarul mesei pe care vrei sa o stergi!", Alert.AlertType.ERROR);
            return;
        }
        try {
            int numar = Integer.parseInt(txtNumarMasa.getText());
            Masa m = masaRepository.read(numar);
            if (m != null) {
                masaRepository.delete(numar);
                afiseazaAlerta("Succes", "Masa " + numar + " a fost stearsa din PostgreSQL!", Alert.AlertType.INFORMATION);
                txtNumarMasa.clear();
                txtCapacitate.clear();
            } else {
                afiseazaAlerta("Eroare", "Masa " + numar + " nu exista in baza de date!", Alert.AlertType.WARNING);
            }
        } catch (NumberFormatException e) {
            afiseazaAlerta("Eroare", "Numarul mesei trebuie sa fie valid!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleAbout(javafx.event.ActionEvent event) {
        String ghidUtilizare = "Ghid de utilizare - Meniu Virtual:\n\n" +
                "1. Adaugare Produse: Selecteaza un produs din lista din stanga si mergi in meniul de sus la File -> Adauga Produs. Poti repeta acest pas de mai multe ori.\n\n" +
                "2. Date Masa: Completeaza numarul mesei si capacitatea in campurile din dreapta.\n\n" +
                "3. Salvare Finala: Apasa pe butonul verde mare 'SALVEAZA REZERVARE'. Aplicatia va trimite masa cu statusul 'Ocupata' in PostgreSQL si iti va afisa bonul fiscal complet!";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cum functioneaza interfata");
        alert.setHeaderText(null);

        javafx.scene.control.Label label = new javafx.scene.control.Label(ghidUtilizare);
        label.setWrapText(true);
        label.setPrefWidth(550);

        alert.getDialogPane().setContent(label);
        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void afiseazaAlerta(String titlu, String mesaj, Alert.AlertType tip) {
        Alert alert = new Alert(tip);
        alert.setTitle(titlu);
        alert.setHeaderText(null);

        javafx.scene.control.Label label = new javafx.scene.control.Label(mesaj);
        label.setWrapText(true);

        alert.getDialogPane().setContent(label);
        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}