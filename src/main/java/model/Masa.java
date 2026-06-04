package model;

public class Masa {
    private int numar;
    private int capacitate;
    private String status;
    private Angajat ospatar;

    public Masa(int numar, int locuri){
        this.numar = numar;
        this.capacitate = locuri;
        this.status = "Libera";
    }

    public int getNumar(){
        return numar;
    }
    public int getCapacitate(){
        return capacitate;
    }
    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public Angajat getOspatar() {
        return ospatar;
    }

    public void setOspatar(Angajat ospatar) {
        this.ospatar = ospatar;
    }

    @Override
    public String toString() {
        String infoOspatar = (ospatar != null) ? ", Ospatar: " + ospatar.getNume() : "";
        return "Masa " + numar + " (" + capacitate + " locuri) - Status: " + status + infoOspatar;
    }
}