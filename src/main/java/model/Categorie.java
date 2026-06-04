package model;
public class Categorie {
    private int id;
    private String denumire;

    public Categorie(String denumire) {
        this.denumire = denumire;
    }

    public Categorie(int id, String denumire) {
        this.id = id;
        this.denumire = denumire;
    }

    public int getId() { return id; }
    public String getDenumire() { return denumire; }
}
