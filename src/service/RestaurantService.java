package service;

import model.*;
import exceptions.MasaOcupataException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


public class RestaurantService implements Promotie {

    private List<Masa> mese;
    private List<Rezervare> rezervari;
    private Meniu meniu;

    public RestaurantService() {
        this.mese = new ArrayList<>();
        this.rezervari = new ArrayList<>();
        this.meniu = new Meniu();
    }


    @Override
    public boolean esteInIntervalulPromotional() {
        LocalTime acum = LocalTime.now();
        return acum.isAfter(LocalTime.of(15, 59)) && acum.isBefore(LocalTime.of(18, 1));
    }

    @Override
    public double aplicaDiscount(Comanda comanda) {
        double discountTotal = 0;
        if (esteInIntervalulPromotional()) {
            for (Produs p : comanda.getProduseComandate()) {
                if (p instanceof Bautura) {
                    discountTotal += p.getPret() * 0.2;
                }
            }
        }
        return discountTotal;
    }

    public double calculeazaNotaPlata(Comanda comanda) {
        double totalFaraDiscount = comanda.getProduseComandate().stream()
                .mapToDouble(Produs::getPret)
                .sum();

        return totalFaraDiscount - aplicaDiscount(comanda);
    }

    public void ocupaMasa(Masa masa) throws MasaOcupataException {
        if (masa.getStatus().equalsIgnoreCase("Ocupata")) {
            throw new MasaOcupataException("Masa " + masa.getNumar() + " este deja ocupata de altcineva!");
        }
        masa.setStatus("Ocupata");
    }


    public List<Bautura> getBauturiAlcoolice() {
        return meniu.getProduse().stream()
                .filter(p -> p instanceof Bautura)
                .map(p -> (Bautura) p)
                .filter(Bautura::isEsteAlcoolica)
                .collect(Collectors.toList());
    }

    public List<Rezervare> cautaRezervareDupaClient(String numeCautat) {
        return rezervari.stream()
                .filter(r -> r.getNumeClient().equalsIgnoreCase(numeCautat))
                .collect(Collectors.toList());
    }

    public Meniu getMeniu() { return meniu; }

    public void adaugaMasa(Masa m) { mese.add(m); }

    public void adaugaRezervare(Rezervare r) { rezervari.add(r); }
}