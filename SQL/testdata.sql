-- testdata.sql
-- Kunde einfügen
INSERT INTO kunde (fname, lname, mail, telefon)
VALUES 
  ('Ali', 'Mustermann', 'ali@test.de', '0123456'),
  ('Mia', 'Schneider', 'mia.schneider@example.com', '0987654');

-- Maschinen einfügen
INSERT INTO maschine (modell, anzahltanks)
VALUES 
  ('Slushomatic 2000', 2),
  ('IceMaster 300', 1);

-- Zubehör einfügen
INSERT INTO zubehor (typ, name, anzahl, preis)
VALUES
  ('Becher', 'Becher 0.2L', 500, 0.05),
  ('Becher', 'Becher 0.3L', 300, 0.07),
  ('Sirup', 'Mango Sirup', 20, 3.00),
  ('Strohhalm', 'Strohhalme', 1000, 0.01);

-- Vermietung einfügen
INSERT INTO vermietung (kid, mid, start, ende)
VALUES 
  (1, 1, '2025-08-23 10:00', '2025-08-24 18:00');

-- Zubehör zur Vermietung zuordnen
INSERT INTO vermietung_zubehor (zid, vid, menge)
VALUES
  (1, 1, 200),   -- 200 Becher 0.2L
  (3, 1, 2);     -- 2 Flaschen Mango-Sirup

-- Abfragen zur Kontrolle
-- Alle Kunden
SELECT * FROM kunde;

-- Welche Maschine wurde vermietet, an wen, wann?
SELECT k.fname, k.lname, m.modell, v.start, v.ende
FROM vermietung v
JOIN kunde k ON v.kid = k.kid
JOIN maschine m ON v.mid = m.mid;

-- Welches Zubehör wurde in der Vermietung genutzt?
SELECT z.name, vz.menge
FROM vermietung_zubehor vz
JOIN zubehor z ON vz.zid = z.zid
WHERE vz.vid = 1;
