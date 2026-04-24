package service;

import service.Comanda;

public interface Promotie {
    double aplicaDiscount(Comanda comanda);
    boolean esteInIntervalulPromotional();
}
