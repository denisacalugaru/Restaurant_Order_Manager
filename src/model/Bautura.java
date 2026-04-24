package model;

public class Bautura extends Produs {

    private boolean esteAlcoolica;

    public Bautura(String nume, double pret, boolean esteAlcoolica) {
        super(nume, pret);
        this.esteAlcoolica = esteAlcoolica;
    }

    public boolean isEsteAlcoolica(){
        return esteAlcoolica;
    }

    @Override
    public String toString(){
        String tip = esteAlcoolica? "[Alcoolic]" : "[Non-Alcoolic]";
        return super.toString() + " " + tip;
    }
}
