package service;

import model.*;
import java.util.List;

public class ProdusFactory {

    public static Produs creeazaProdus(String tip, String nume, double pret, Object optiuneSuplimentara) {
        if ("BAUTURA".equalsIgnoreCase(tip)) {
            boolean esteAlcoolica = (optiuneSuplimentara instanceof Boolean) ? (Boolean) optiuneSuplimentara : false;
            return new Bautura(nume, pret, esteAlcoolica);
        } else if ("PREPARAT".equalsIgnoreCase(tip)) {
            List<Ingredient> ingrediente = (optiuneSuplimentara instanceof List) ? (List<Ingredient>) optiuneSuplimentara : null;
            return new PreparatCulinar(nume, pret, null, ingrediente);
        }
        throw new IllegalArgumentException("Tipul de produs " + tip + " este necunoscut!");
    }
}