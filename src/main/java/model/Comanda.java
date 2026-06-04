package model;

import java.util.ArrayList;
import java.util.List;

public class Comanda {

    private Masa masa;
    private List<Produs> produseComandate;

    public Comanda(Masa masa){
        this.masa = masa;
        this.produseComandate = new ArrayList<>();
    }
    public void adaugaProdus(Produs p){
        produseComandate.add(p);
    }

    public void eliminaProdus(Produs p){
        produseComandate.remove(p);
    }

    public Masa getMasa(){
        return masa;
    }

    public List<Produs> getProduseComandate() {
        return produseComandate;
    }

    @Override
    public String toString(){
        return " Comanda pentru " + masa.toString() + " are " + produseComandate.size() + "produse.";

    }
}
