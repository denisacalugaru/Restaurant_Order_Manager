package service;
import model.Comanda;

public interface Promotie {
    double aplicaDiscount(Comanda comanda);
    boolean esteInIntervalulPromotional();
}
