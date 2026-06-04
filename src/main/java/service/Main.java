package service;

import model.*;
import repository.*;
import exceptions.MasaOcupataException;
import java.util.Arrays;
import java.util.List;
import java.time.LocalTime;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/restaurant-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);
        stage.setTitle("Restaurant Order Manager - JavaFX Edition");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        RestaurantService restaurant = new RestaurantService();
        MasaRepository masaRepository = MasaRepository.getInstance();
        RezervareRepository rezervareRepository = RezervareRepository.getInstance();
        ProdusRepository produsRepository = ProdusRepository.getInstance();
        AngajatRepository angajatRepository = AngajatRepository.getInstance();
        IngredientRepository ingredientRepository = IngredientRepository.getInstance();
        CategorieRepository categorieRepository = CategorieRepository.getInstance();

        System.out.println("=== Restaurant Order Manager - Demo consola ===\n");

        Ingredient ou = new Ingredient("Ou", 100, false);
        Ingredient rosie = new Ingredient("Rosie", 50, true);
        Ingredient branza = new Ingredient("Branza", 30, false);
        Ingredient avocado = new Ingredient("Avocado", 20, true);
        Ingredient ciuperci = new Ingredient("Ciuperci", 30, true);

        // Actiunea 1: adaugare produs in meniu
        System.out.println("[1] Adaugare produse in meniu...");
        Produs omleta = ProdusFactory.creeazaProdus("PREPARAT", "Omleta Casei", 22.0, Arrays.asList(ou, rosie, branza));
        Produs salataVegana = ProdusFactory.creeazaProdus("PREPARAT", "Salata Vegana", 28.0, Arrays.asList(avocado, rosie, ciuperci));
        Produs cafea = ProdusFactory.creeazaProdus("BAUTURA", "Cafea Espresso", 10.0, false);
        Produs vin = ProdusFactory.creeazaProdus("BAUTURA", "Vin Rosu", 25.0, true);
        Produs apa = ProdusFactory.creeazaProdus("BAUTURA", "Apa Minerala", 8.0, false);
        Produs deSters = ProdusFactory.creeazaProdus("PREPARAT", "Produs Temporar", 15.0, Arrays.asList(rosie));

        restaurant.getMeniu().adaugaProdus(omleta);
        restaurant.getMeniu().adaugaProdus(salataVegana);
        restaurant.getMeniu().adaugaProdus(cafea);
        restaurant.getMeniu().adaugaProdus(vin);
        restaurant.getMeniu().adaugaProdus(apa);
        restaurant.getMeniu().adaugaProdus(deSters);

        try {
            produsRepository.create(omleta);
            produsRepository.create(salataVegana);
            produsRepository.create(cafea);
            produsRepository.create(vin);
            produsRepository.create(apa);
            produsRepository.create(deSters);
            System.out.println("-> produse salvate.\n");
        } catch (Exception e) {
            System.out.println("-> produsele existau deja in DB.\n");
        }

        // Actiunea 2: stergere produs din meniu
        System.out.println("[2] Stergere produs din meniu...");
        restaurant.getMeniu().getProduse().removeIf(p -> p.getNume().equals("Produs Temporar"));
        try {
            produsRepository.delete(999);
            System.out.println("-> 'Produs Temporar' eliminat.\n");
        } catch (Exception e) {
            System.out.println("-> " + e.getMessage() + "\n");
        }

        // Actiunea 3: actualizare pret produs
        System.out.println("[3] Actualizare pret produs...");
        omleta.setPret(25.0);
        try {
            produsRepository.update(omleta);
            System.out.println("-> pret actualizat la " + omleta.getPret() + " RON\n");
        } catch (Exception e) {
            System.out.println("-> eroare: " + e.getMessage() + "\n");
        }

        for (int i = 1; i <= 10; i++) {
            Masa existenta = masaRepository.read(i);
            if (existenta == null) {
                int cap = (i % 2 == 0) ? 4 : 2;
                Masa m = new Masa(i, cap);
                m.setStatus("Libera");
                masaRepository.create(m);
            }
        }
        List<Masa> toateMesele = masaRepository.readAll();
        for (Masa m : toateMesele) {
            restaurant.adaugaMasa(m);
        }

        // Actiunea 4: creare rezervare
        System.out.println("[4] Creare rezervare...");
        Masa masaRezervare = toateMesele.get(2);
        Rezervare rezMaria = new Rezervare.Builder()
                .setNumeClient("Maria")
                .setTelefon("0722123456")
                .setDataOra("05-06 ora 19:00")
                .setMasaRezervata(masaRezervare)
                .build();

        Rezervare rezDeAnulat = new Rezervare.Builder()
                .setNumeClient("Client Temporar")
                .setTelefon("0733111222")
                .setDataOra("05-06 ora 21:00")
                .setMasaRezervata(toateMesele.get(3))
                .build();

        restaurant.adaugaRezervare(rezMaria);
        restaurant.adaugaRezervare(rezDeAnulat);
        try {
            rezervareRepository.create(rezMaria);
            rezervareRepository.create(rezDeAnulat);
            System.out.println("-> rezervari salvate.\n");
        } catch (Exception e) {
            System.out.println("-> existau deja in DB.\n");
        }

        // Actiunea 5: anulare rezervare
        System.out.println("[5] Anulare rezervare...");
        restaurant.cautaRezervareDupaClient("Client Temporar").clear();
        try {
            rezervareRepository.delete(999);
            System.out.println("-> rezervare anulata.\n");
        } catch (Exception e) {
            System.out.println("-> " + e.getMessage() + "\n");
        }

        // Actiunea 6: deschidere comanda la masa
        System.out.println("[6] Deschidere comanda la masa...");
        Masa m1 = toateMesele.get(0);
        Comanda comandaMasa1 = new Comanda(m1);
        System.out.println("-> comanda deschisa pentru Masa " + comandaMasa1.getMasa().getNumar() + ".\n");

        // Actiunea 7: adaugare produse in comanda
        System.out.println("[7] Adaugare produse in comanda...");
        comandaMasa1.adaugaProdus(salataVegana);
        comandaMasa1.adaugaProdus(vin);
        comandaMasa1.adaugaProdus(apa);
        System.out.println("-> adaugate: Salata Vegana, Vin Rosu, Apa Minerala.\n");

        // Actiunea 8: eliminare produs din comanda
        System.out.println("[8] Eliminare produs din comanda...");
        comandaMasa1.eliminaProdus(apa);
        System.out.println("-> Apa Minerala eliminata.\n");

        // Actiunea 9: calcul nota de plata
        System.out.println("[9] Calcul nota de plata...");
        double totalNota = restaurant.calculeazaNotaPlata(comandaMasa1);
        System.out.println("-> total: " + String.format("%.2f", totalNota) + " RON\n");

        // Actiunea 10: modificare status masa
        System.out.println("[10] Modificare status masa...");
        m1.setStatus("Ocupata");
        masaRepository.update(m1);
        System.out.println("-> Masa " + m1.getNumar() + " marcata ca: " + m1.getStatus() + "\n");

        // Actiunea 11: aplicare discount happy hour
        System.out.println("[11] Verificare discount Happy Hour (16:00-18:00)...");
        LocalTime oraCurenta = LocalTime.now();
        System.out.println("-> ora curenta: " + oraCurenta);
        if (oraCurenta.isAfter(LocalTime.of(16, 0)) && oraCurenta.isBefore(LocalTime.of(18, 0))) {
            System.out.println("-> discount activ! -20% la bauturi.");
        } else {
            System.out.println("-> in afara intervalului, discount inactiv.");
        }
        System.out.println();

        // Actiunea 12: sortare produse din meniu
        System.out.println("[12] Meniu sortat alfabetic:");
        restaurant.getMeniu().getProduse().stream()
                .map(Produs::getNume)
                .sorted()
                .forEach(nume -> System.out.println("  - " + nume));
        System.out.println();

        // Actiunea 13: filtrare bauturi alcoolice
        System.out.println("[13] Bauturi alcoolice:");
        restaurant.getBauturiAlcoolice().forEach(b -> System.out.println("  - " + b.getNume()));
        System.out.println();

        // Actiunea 14: cautare rezervare dupa client
        System.out.println("[14] Cautare rezervare dupa nume client...");
        List<Rezervare> rezervariGasite = restaurant.cautaRezervareDupaClient("Maria");
        rezervariGasite.forEach(r -> System.out.println("  -> " + r.getNumeClient() + " - masa " + r.getMasaRezervata().getNumar()));
        System.out.println();

        // Actiunea 15: filtrare produse vegane
        System.out.println("[15] Preparate vegane:");
        restaurant.getMeniu().getProduseVegane().forEach(p -> System.out.println("  - " + p.getNume()));
        System.out.println();

        // test exceptie custom
        System.out.println("[Test] MasaOcupataException:");
        Angajat ospatar = new Angajat("Andrei", "Ospatar", 3500.0);
        try {
            restaurant.ocupaMasaCuOspatar(m1, ospatar);
            restaurant.ocupaMasaCuOspatar(m1, ospatar);
        } catch (MasaOcupataException e) {
            System.out.println("  -> exceptie prinsa: " + e.getMessage());
        }
        System.out.println();

        System.out.println("================================================================");
        System.out.println("CRUD - ANGAJATI, INGREDIENTE, CATEGORII");
        System.out.println("================================================================\n");

        // CRUD Angajat
        System.out.println("--- Angajat Repository ---");
        Angajat angajat1 = new Angajat("Mihai Popescu", "Ospatar", 3200.0);
        Angajat angajat2 = new Angajat("Elena Ionescu", "Bucatar Sef", 5000.0);
        Angajat angajatDeModificat = new Angajat("Ion Temporar", "Ajutor Bucatar", 2500.0);
        try {
            angajatRepository.create(angajat1);
            angajatRepository.create(angajat2);
            angajatRepository.create(angajatDeModificat);
            System.out.println("[CREATE] angajati inserati.");
        } catch (Exception e) {
            System.out.println("[CREATE] existau deja in DB.");
        }

        System.out.println("[READ ALL]");
        angajatRepository.readAll().forEach(a ->
                System.out.println("  - " + a.getNume() + " | " + a.getRol() + " | " + a.getSalariu() + " RON")
        );

        System.out.println("[UPDATE] promovare Ion Temporar...");
        angajatRepository.update(new Angajat("Ion Temporar", "Bucatar", 3800.0));
        angajatRepository.readAll().forEach(a ->
                System.out.println("  - " + a.getNume() + " | " + a.getRol() + " | " + a.getSalariu() + " RON")
        );

        System.out.println("[READ by ID] id=1:");
        Angajat citit = angajatRepository.read(1);
        System.out.println(citit != null ? "  -> " + citit.getNume() + " - " + citit.getRol() : "  -> negasit.");

        System.out.println("[DELETE] id=999...");
        angajatRepository.delete(999);
        System.out.println("-> ok.\n");

        // CRUD Ingredient
        System.out.println("--- Ingredient Repository ---");
        Ingredient ing1 = new Ingredient("Faina", 500.0, true);
        Ingredient ing2 = new Ingredient("Carne Vita", 200.0, false);
        Ingredient ing3 = new Ingredient("Ulei Masline", 300.0, true);
        try {
            ingredientRepository.create(ing1);
            ingredientRepository.create(ing2);
            ingredientRepository.create(ing3);
            System.out.println("[CREATE] ingrediente inserate.");
        } catch (Exception e) {
            System.out.println("[CREATE] existau deja in DB.");
        }

        System.out.println("[READ ALL]");
        ingredientRepository.readAll().forEach(i ->
                System.out.println("  - " + i.getNume() + " | stoc: " + i.getStoc() + "g | vegan: " + (i.isEsteVegan() ? "da" : "nu"))
        );

        System.out.println("[UPDATE] modificare stoc Faina...");
        ingredientRepository.update(new Ingredient("Faina", 350.0, true));
        ingredientRepository.readAll().forEach(i ->
                System.out.println("  - " + i.getNume() + " | stoc: " + i.getStoc() + "g | vegan: " + (i.isEsteVegan() ? "da" : "nu"))
        );

        System.out.println("[READ by ID] id=1:");
        Ingredient ingCitit = ingredientRepository.read(1);
        System.out.println(ingCitit != null ? "  -> " + ingCitit.getNume() + " | vegan: " + ingCitit.isEsteVegan() : "  -> negasit.");

        System.out.println("[DELETE] id=999...");
        ingredientRepository.delete(999);
        System.out.println("-> ok.\n");

        // CRUD Categorie
        System.out.println("--- Categorie Repository ---");
        try {
            categorieRepository.create(new Categorie("Aperitive"));
            categorieRepository.create(new Categorie("Fel Principal"));
            categorieRepository.create(new Categorie("Deserturi"));
            System.out.println("[CREATE] categorii inserate.");
        } catch (Exception e) {
            System.out.println("[CREATE] existau deja in DB.");
        }

        System.out.println("[READ ALL]");
        categorieRepository.readAll().forEach(c -> System.out.println("  - " + c.getDenumire()));

        System.out.println("[READ by ID] id=1:");
        Categorie catCitita = categorieRepository.read(1);
        System.out.println(catCitita != null ? "  -> " + catCitita.getDenumire() : "  -> negasita.");

        System.out.println("[UPDATE] redenumire categorie...");
        categorieRepository.update(new Categorie(1, "Aperitive si Supe"));
        categorieRepository.readAll().forEach(c -> System.out.println("  - " + c.getDenumire()));

        System.out.println("[DELETE] id=999...");
        categorieRepository.delete(999);
        System.out.println("-> ok.\n");

        System.out.println("================================================================");
        System.out.println("CRUD demonstrat pentru: Masa, Produs, Rezervare, Angajat, Ingredient, Categorie");
        System.out.println("================================================================\n");

        System.out.println("Stare mese (din DB):");
        masaRepository.readAll().forEach(m -> System.out.println("  Masa " + m.getNumar() + " - " + m.getStatus()));

        System.out.println("\n-> pornire interfata grafica...\n");
        launch(args);
    }
}