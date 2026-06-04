package model;

import java.util.List;

public class PreparatCulinar extends Produs {

    private Categorie categorie;
    private List<Ingredient> ingrediente;

    public PreparatCulinar(String nume, double pret, Categorie categorie, List<Ingredient> ingrediente){
        super(nume, pret);
        this.categorie = categorie;
        this.ingrediente = ingrediente;
    }

    public Categorie getCategorie(){

        return categorie;
    }
    public List<Ingredient> getIngrediente(){

        return ingrediente;
    }

    public boolean esteVegan(){
        for(Ingredient i : ingrediente){
            if(!i.isEsteVegan()){
                return false;
            }
        }
        return true;
    }

}
