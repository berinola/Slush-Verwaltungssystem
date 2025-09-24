-- =========================
-- CLEANUP (FK-safe order)
-- =========================
DELETE FROM vermietung_zubehor;
DELETE FROM vermietung;
DELETE FROM zubehor;
DELETE FROM maschine;
DELETE FROM kunde;

-- Optional: reset identity sequences (PostgreSQL default names)
-- If you get "sequence does not exist", comment out the offending line(s).
ALTER SEQUENCE IF EXISTS kunde_kid_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS maschine_mid_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS zubehor_zid_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS vermietung_vid_seq RESTART WITH 1;

-- =========================
-- KUNDEN
-- =========================
INSERT INTO kunde (fname, lname, mail, telefon) VALUES
                                                    ('Ali', 'Mustermann', 'ali@test.de', '0123456'),
                                                    ('Mia', 'Schneider',  'mia.schneider@example.com', '0987654');

-- =========================
-- MASCHINEN (anzahl_tanks must be 1 or 2)
-- =========================
INSERT INTO maschine (modell, anzahl_tanks) VALUES
                                                ('Slushomatic 2000', 2),
                                                ('IceMaster 300',    1);

-- =========================
-- ZUBEHÖR
-- =========================
INSERT INTO zubehor (typ, name, anzahl, preis) VALUES
                                                   ('Becher',     'Becher 0.2L', 500, 0.05),
                                                   ('Becher',     'Becher 0.3L', 300, 0.07),
                                                   ('Sirup',      'Mango',         50, 3.00),
                                                   ('Sirup',      'Cola',          40, 3.00),
                                                   ('Strohhalme', 'Strohhalme',   800, 0.01);

-- =========================
-- VERMIETUNGEN (start < ende)
-- =========================
-- Ali mietet Slushomatic 2000 am 2025-09-05
INSERT INTO vermietung (kid, mid, start, ende)
VALUES (
           (SELECT kid FROM kunde WHERE mail = 'ali@test.de'),
           (SELECT mid FROM maschine WHERE modell = 'Slushomatic 2000'),
           TIMESTAMP '2025-09-05 10:00:00',
           TIMESTAMP '2025-09-05 18:00:00'
       );

-- Mia mietet IceMaster 300 am 2025-09-06
INSERT INTO vermietung (kid, mid, start, ende)
VALUES (
           (SELECT kid FROM kunde WHERE mail = 'mia.schneider@example.com'),
           (SELECT mid FROM maschine WHERE modell = 'IceMaster 300'),
           TIMESTAMP '2025-09-06 09:00:00',
           TIMESTAMP '2025-09-06 17:00:00'
       );

-- =========================
-- VERMietung_ZUBEHÖR (PK: (zid, vid))
-- =========================
-- Zubehör für Alis Vermietung:
INSERT INTO vermietung_zubehor (zid, vid, menge) VALUES
    ((SELECT zid FROM zubehor WHERE name = 'Becher 0.2L'),
     (SELECT vid FROM vermietung
      WHERE kid = (SELECT kid FROM kunde WHERE mail = 'ali@test.de')
        AND mid = (SELECT mid FROM maschine WHERE modell = 'Slushomatic 2000')
        AND start = TIMESTAMP '2025-09-05 10:00:00'),
     200);

INSERT INTO vermietung_zubehor (zid, vid, menge) VALUES
    ((SELECT zid FROM zubehor WHERE name = 'Mango'),
     (SELECT vid FROM vermietung
      WHERE kid = (SELECT kid FROM kunde WHERE mail = 'ali@test.de')
        AND mid = (SELECT mid FROM maschine WHERE modell = 'Slushomatic 2000')
        AND start = TIMESTAMP '2025-09-05 10:00:00'),
     2);

-- Zubehör für Mias Vermietung:
INSERT INTO vermietung_zubehor (zid, vid, menge) VALUES
    ((SELECT zid FROM zubehor WHERE name = 'Becher 0.3L'),
     (SELECT vid FROM vermietung
      WHERE kid = (SELECT kid FROM kunde WHERE mail = 'mia.schneider@example.com')
        AND mid = (SELECT mid FROM maschine WHERE modell = 'IceMaster 300')
        AND start = TIMESTAMP '2025-09-06 09:00:00'),
     150);

INSERT INTO vermietung_zubehor (zid, vid, menge) VALUES
    ((SELECT zid FROM zubehor WHERE name = 'Cola'),
     (SELECT vid FROM vermietung
      WHERE kid = (SELECT kid FROM kunde WHERE mail = 'mia.schneider@example.com')
        AND mid = (SELECT mid FROM maschine WHERE modell = 'IceMaster 300')
        AND start = TIMESTAMP '2025-09-06 09:00:00'),
     3);

-- Optional: Bestände im Lager direkt anpassen (falls du realistisch starten willst)
-- UPDATE zubehor SET anzahl = anzahl - 200 WHERE name = 'Becher 0.2L';
-- UPDATE zubehor SET anzahl = anzahl - 2   WHERE name = 'Mango';
-- UPDATE zubehor SET anzahl = anzahl - 150 WHERE name = 'Becher 0.3L';
-- UPDATE zubehor SET anzahl = anzahl - 3   WHERE name = 'Cola';
