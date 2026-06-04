package model;

public class Angajat {
    private String nume;
    private String rol;
    private double salariu;

    public Angajat(String nume, String rol, double salariu){
        this.nume = nume;
        this.rol = rol;
        this.salariu = salariu;

    }

    public String getNume(){
        return nume;
    }

    public String getRol(){

        return rol;
    }
    public double getSalariu() {
        return salariu;
    }
}

