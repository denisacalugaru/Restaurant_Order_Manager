package model;

public class Masa {
    private int numar;
    private int capacitate;
    private String status;

    public Masa(int numar, int locuri){
        this.numar = numar;
        this.capacitate = capacitate;
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

    @Override
    public String toString() {
        return "Masa " + numar + " (" + capacitate + " locuri) - Status: " + status;
    }

}
