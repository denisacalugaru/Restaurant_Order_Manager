package model;
import java.time.LocalDateTime;

public class Rezervare {

    private String numeClient;
    private String telefon;
    private String dataOra;
    private Masa masaRezervata;

    public Rezervare(String numeClient, String telefon, String dataOra, Masa masaRezervata){
        this.numeClient = numeClient;
        this.telefon = telefon;
        this.dataOra = dataOra;
        this.masaRezervata = masaRezervata;
    }

    public String getNumeClient(){
        return numeClient;
    }

    public String getDataOra(){
        return dataOra;
    }

    public Masa getMasaRezervata(){
        return masaRezervata;
    }
}
