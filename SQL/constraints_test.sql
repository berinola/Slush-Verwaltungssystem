-- constraints_test.sql
-- Diese Inserts sollen absichtlich fehlschlagen, um die Constraints zu prüfen.

-- Ungültige E-Mail (kein @)
INSERT INTO kunde (fname, lname, mail, telefon)
VALUES ('Test', 'User', 'keineMail', '0123456');

-- Tanks = 0 (soll fehlschlagen, weil CHECK (AnzahlTanks > 0))
INSERT INTO maschine (modell, anzahltanks)
VALUES ('Defekte Maschine', 0);

-- Tanks = 5 (soll fehlschlagen, weil CHECK (AnzahlTanks < 3))
INSERT INTO maschine (modell, anzahltanks)
VALUES ('Super-Maschine', 5);

-- Preis = -1 (soll fehlschlagen, weil Preis > 0)
INSERT INTO zubehor (typ, name, anzahl, preis)
VALUES ('Becher', 'Fehlerbecher', 100, -1);

-- Vermietung mit falschem Zeitraum (start > ende)
INSERT INTO vermietung (kid, mid, start, ende)
VALUES (1, 1, '2025-08-25 18:00', '2025-08-24 10:00');
