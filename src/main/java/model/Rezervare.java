package model;

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

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getDataOra(){
        return dataOra;
    }

    public void setDataOra(String dataOra) {
        this.dataOra = dataOra;
    }

    public Masa getMasaRezervata(){
        return masaRezervata;
    }

    public void setMasaRezervata(Masa masaRezervata) {
        this.masaRezervata = masaRezervata;
    }

    public static class Builder {
        private String numeClient;
        private String telefon;
        private String dataOra;
        private Masa masaRezervata;

        public Builder setNumeClient(String numeClient) {
            this.numeClient = numeClient;
            return this;
        }

        public Builder setTelefon(String telefon) {
            this.telefon = telefon;
            return this;
        }

        public Builder setDataOra(String dataOra) {
            this.dataOra = dataOra;
            return this;
        }

        public Builder setMasaRezervata(Masa masaRezervata) {
            this.masaRezervata = masaRezervata;
            return this;
        }

        public Rezervare build() {
            return new Rezervare(numeClient, telefon, dataOra, masaRezervata);
        }
    }
}