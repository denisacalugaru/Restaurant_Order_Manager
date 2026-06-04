package service;

import model.PreparatCulinar;
import model.Produs;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Meniu {

    private Set<Produs> produse;

    public Meniu(){
        this.produse = new TreeSet<>();
    }

    public void adaugaProdus(Produs p){
        produse.add(p);
    }

    public void stergeProdus(Produs p){
        produse.remove(p);
    }
    public List<PreparatCulinar> getProduseVegane(){

        return produse.stream()
                .filter(p -> p instanceof PreparatCulinar)
                .map(p -> (PreparatCulinar) p)
                .filter(PreparatCulinar::esteVegan)
                .collect(Collectors.toList());
    }

    public Set<Produs> getProduse() {
        return produse;
    }
}
