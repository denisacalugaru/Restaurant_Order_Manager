import model.*;
import service.*;
import exceptions.MasaOcupataException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RestaurantService restaurant = new RestaurantService();

        Ingredient ou = new Ingredient("Ou", 100, false);
        Ingredient rosie = new Ingredient("Rosie", 50, true);
        Ingredient branza = new Ingredient("Branza", 30, false);
        Ingredient pui = new Ingredient("Piept de Pui", 20, false);
        Ingredient paste = new Ingredient("Paste", 40, true);
        Ingredient avocado = new Ingredient("Avocado", 20, true);
        Ingredient ciuperci = new Ingredient("Ciuperci", 30, true);

        Categorie micDejun = new Categorie("Mic Dejun");
        Categorie pranz = new Categorie("Pranz");
        Categorie cina = new Categorie("Cina");
        Categorie vegan = new Categorie("Vegan");


        PreparatCulinar omleta = new PreparatCulinar("Omleta Casei", 22.0, micDejun, Arrays.asList(ou, rosie, branza));
        PreparatCulinar pastePui = new PreparatCulinar("Paste cu Pui", 35.0, pranz, Arrays.asList(paste, pui, branza));
        PreparatCulinar salataVegana = new PreparatCulinar("Salata Vegana", 28.0, vegan, Arrays.asList(avocado, rosie, ciuperci));


        Bautura cafea = new Bautura("Cafea Espresso", 10.0, false);
        Bautura vin = new Bautura("Vin Rosu", 25.0, true);
        Bautura apa = new Bautura("Apa Minerala", 8.0, false);

        restaurant.getMeniu().adaugaProdus(omleta);
        restaurant.getMeniu().adaugaProdus(pastePui);
        restaurant.getMeniu().adaugaProdus(salataVegana);
        restaurant.getMeniu().adaugaProdus(cafea);
        restaurant.getMeniu().adaugaProdus(vin);
        restaurant.getMeniu().adaugaProdus(apa);

        Masa m1 = new Masa(1, 2);
        Masa m2 = new Masa(2, 4);
        restaurant.adaugaMasa(m1);
        restaurant.adaugaMasa(m2);

        System.out.println("TESTARE GESTIUNE MESE SI EXCEPTII");
        try {
            restaurant.ocupaMasa(m1);
            System.out.println("Masa 1 ocupata cu succes.");
            restaurant.ocupaMasa(m1);
        } catch (MasaOcupataException e) {
            System.out.println( e.getMessage());
        }

        System.out.println("\nCAUTARE REZERVARE");
        Rezervare rez1 = new Rezervare("Maria", "0722123456", "25-04 ora 19", m1);
        restaurant.adaugaRezervare(rez1);

        List<Rezervare> gasite = restaurant.cautaRezervareDupaClient("Maria");
        gasite.forEach(r -> System.out.println("Rezervare gasita pentru: " + r.getNumeClient() + " la masa " + r.getMasaRezervata().getNumar()));

        Comanda c1 = new Comanda(m1);
        c1.adaugaProdus(salataVegana);
        c1.adaugaProdus(vin);
        c1.adaugaProdus(apa);


        System.out.println("\nNOTA DE PLATA");
        double total = restaurant.calculeazaNotaPlata(c1);
        System.out.println("Masa " + c1.getMasa().getNumar() + " | Total Final: " + String.format("%.2f", total) + " RON");

        System.out.println("Produse Vegane gasite:");
        restaurant.getMeniu().getProduseVegane().forEach(p -> System.out.println("- " + p.getNume()));

        System.out.println("\nBauturi Alcoolice gasite:");
        restaurant.getBauturiAlcoolice().forEach(b -> System.out.println("- " + b.getNume()));

        System.out.println("\nMENIU COMPLET SORTAT ALFABETIC ");
        restaurant.getMeniu().getProduse().forEach(System.out::println);
    }

}