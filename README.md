# Restaurant Order Manager

Proiect realizat in Java pentru materia Programare Orientata pe Obiecte II, 2026.

## Descrierea sistemului

Aplicatie de tip Restaurant Order Manager care permite gestionarea meselor, comenzilor, rezervarilor, meniului si angajatilor dintr-un restaurant.

### Actiuni implementate in sistem

1. Adaugare produs in meniu
2. Stergere produs din meniu
3. Actualizare pret produs
4. Creare rezervare
5. Anulare rezervare
6. Deschidere comanda la masa
7. Adaugare produs in comanda
8. Eliminare produs din comanda
9. Calcul nota de plata
10. Modificare status masa (Libera/Ocupata)
11. Aplicare discount Happy Hour (16:00-18:00, -20% la bauturi)
12. Sortare alfabetica a produselor din meniu
13. Filtrare bauturi alcoolice
14. Cautare rezervare dupa numele clientului
15. Filtrare preparate vegane din meniu

### Tipuri de obiecte definite

Produs, PreparatCulinar, Bautura, Ingredient, Categorie, Masa, Comanda, Rezervare, Angajat, Meniu

---

## Cerinte respectate

### Cerinta 1 - Definirea sistemului
- 15 actiuni definite si demonstrate in `Main.java`
- 10 tipuri de obiecte implementate in pachetul `model/`

### Cerinta 2 - Implementare Java
- **Encapsulare** - toate campurile din clasele model sunt `private`, accesibile doar prin getteri si setteri
- **3 colectii diferite** - `HashMap<Integer, Masa>` in `RestaurantService`, `ArrayList<Rezervare>` in `RestaurantService`, `TreeSet<Produs>` in `Meniu`
- **Colectie sortata** - `TreeSet` din `Meniu` sorteaza produsele alfabetic automat prin `compareTo` din `Produs`
- **Mostenire** - `Bautura` si `PreparatCulinar` extind clasa abstracta `Produs`
- **Interfata** - interfata `Promotie` implementata de `RestaurantService`, interfata generica `GenericRepository<T, ID>` implementata de toate repository-urile
- **Exceptie custom** - `MasaOcupataException` definita in `exceptions/` si aruncata in `ocupaMasa()` din `RestaurantService`
- **Clasa serviciu** - `RestaurantService` expune toate operatiile sistemului
- **Clasa Main** - `Main.java` demonstreaza toate cele 15 actiuni si apeleaza serviciile

### Cerinta 3 - Persistenta cu baza de date relationala si JDBC
- Baza de date **PostgreSQL**
- Conexiune gestionata prin `DatabaseConnection.java` (Singleton)
- Interfata generica `GenericRepository<T, ID>` defineste contractul CRUD
- **CRUD complet** implementat pentru 6 obiecte: `Masa`, `Produs`, `Rezervare`, `Angajat`, `Ingredient`, `Categorie`
- Fiecare repository foloseste `PreparedStatement` pentru securitate (previne SQL injection)

### Cerinta 4 - Serviciu de audit
- `AuditService.java` scrie in fisierul `audit.csv` la fiecare actiune executata
- Format: `nume_actiune,timestamp`
- Implementat ca Singleton, metoda `scrieActiune()` este `synchronized` (thread-safe)

### Cerinta 5 - Design Patterns
- **Singleton** - `AuditService`, `DatabaseConnection`, toate repository-urile
- **Builder** - `Rezervare.Builder` folosit la crearea rezervarilor (actiunea 4)
- **Factory** - `ProdusFactory.creeazaProdus()` folosit la crearea produselor din meniu

### Cerinta 6 - Interfata grafica JavaFX
- 1 fereastra principala
- Meniu cu 3 optiuni: File, Edit, Help
- Lista (`ListView`) pentru afisarea meniului
- Formular cu campuri (`TextField`) si buton pentru salvarea rezervarilor

---

## Diagrama ERD
<img width="1049" height="594" alt="ERD" src="https://github.com/user-attachments/assets/8d85e639-23e6-4eec-8e45-a6531695018c" />


## Tehnologii folosite

- Java 21
- JavaFX 21
- PostgreSQL
- JDBC
- IntelliJ IDEA
