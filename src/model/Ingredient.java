package model;

public class Ingredient {

    private String nume;
    private double stoc;
    private boolean esteVegan;

    public Ingredient(String nume, double stoc, boolean esteVegan){
        this.nume = nume;
        this.stoc = stoc;
        this.esteVegan = esteVegan;
    }

    public String getNume(){return nume;}

    public double getStoc(){return stoc;}

    public boolean isEsteVegan(){
        return esteVegan;
    }

    public void setStoc(double stoc){
        this.stoc = stoc;
    }

    @Override
    public String toString(){
        return nume + " (Vegan: " + (esteVegan ? "Da" : "Nu") + ")";
    }

}
