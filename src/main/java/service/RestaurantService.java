package service;

import model.*;
import exceptions.MasaOcupataException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantService implements Promotie {

    private Map<Integer, Masa> mese;
    private List<Rezervare> rezervari;
    private Meniu meniu;
    private final AuditService auditService = AuditService.getInstance();

    public RestaurantService() {
        this.mese = new HashMap<>();
        this.rezervari = new ArrayList<>();
        this.meniu = new Meniu();
    }

    @Override
    public boolean esteInIntervalulPromotional() {
        return LocalTime.now().isAfter(LocalTime.of(15, 59)) && LocalTime.now().isBefore(LocalTime.of(18, 1));
    }

    @Override
    public double aplicaDiscount(Comanda comanda) {
        auditService.scrieActiune("aplicaDiscount");
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
        auditService.scrieActiune("calculeazaNotaPlata");
        double totalFaraDiscount = comanda.getProduseComandate().stream()
                .mapToDouble(Produs::getPret)
                .sum();

        return totalFaraDiscount - aplicaDiscount(comanda);
    }

    public void ocupaMasa(Masa masa) throws MasaOcupataException {
        auditService.scrieActiune("ocupaMasa");
        if (masa.getStatus().equalsIgnoreCase("Ocupata")) {
            throw new MasaOcupataException("Masa " + masa.getNumar() + " este deja ocupata de altcineva!");
        }
        masa.setStatus("Ocupata");
    }

    public void ocupaMasaCuOspatar(Masa masa, Angajat angajat) throws MasaOcupataException {
        auditService.scrieActiune("alocareOspatarMasa");
        if (masa.getStatus().equalsIgnoreCase("Ocupata")) {
            throw new MasaOcupataException("Masa " + masa.getNumar() + " este deja ocupata de altcineva!");
        }
        masa.setStatus("Ocupata");
        masa.setOspatar(angajat);
    }

    public List<Bautura> getBauturiAlcoolice() {
        auditService.scrieActiune("getBauturiAlcoolice");
        return meniu.getProduse().stream()
                .filter(p -> p instanceof Bautura)
                .map(p -> (Bautura) p)
                .filter(Bautura::isEsteAlcoolica)
                .collect(Collectors.toList());
    }

    public List<Rezervare> cautaRezervareDupaClient(String numeCautat) {
        auditService.scrieActiune("cautaRezervareDupaClient");
        return rezervari.stream()
                .filter(r -> r.getNumeClient().equalsIgnoreCase(numeCautat))
                .collect(Collectors.toList());
    }

    public Meniu getMeniu() {
        auditService.scrieActiune("getMeniu");
        return meniu;
    }

    public void adaugaMasa(Masa m) {
        auditService.scrieActiune("adaugaMasa");
        mese.put(m.getNumar(), m);
    }

    public Masa gasesteMasaDupaNumar(int numar) {
        auditService.scrieActiune("gasesteMasaDupaNumar");
        return mese.get(numar);
    }

    public void adaugaRezervare(Rezervare r) {
        auditService.scrieActiune("adaugaRezervare");
        rezervari.add(r);
    }

    public Collection<Masa> getMese() {
        auditService.scrieActiune("getMese");
        return mese.values();
    }
}