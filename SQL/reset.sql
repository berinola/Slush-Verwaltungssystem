-- reset.sql
-- Bestehende Tabellen löschen (Reihenfolge wegen Foreign Keys wichtig)
DROP TABLE IF EXISTS vermietung_zubehor CASCADE;
DROP TABLE IF EXISTS vermietung CASCADE;
DROP TABLE IF EXISTS zubehor CASCADE;
DROP TABLE IF EXISTS maschine CASCADE;
DROP TABLE IF EXISTS kunde CASCADE;

-- Tabellen neu anlegen

CREATE TABLE maschine (
    MID INT GENERATED ALWAYS AS IDENTITY,
    Modell VARCHAR(50),
    AnzahlTanks INT CHECK (AnzahlTanks > 0 AND AnzahlTanks < 3),
    PRIMARY KEY (MID)
);

CREATE TABLE kunde (
    KID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Fname TEXT,
    Lname TEXT,
    Mail TEXT CHECK (Mail ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    Telefon VARCHAR(20)
);

CREATE TABLE zubehor (
    ZID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Typ TEXT,
    Name TEXT,
    Anzahl INT,
    Preis DOUBLE PRECISION CHECK (Preis > 0)
);

CREATE TABLE vermietung (
    VID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    KID INT,
    MID INT,
    start TIMESTAMP,
    ende TIMESTAMP,
    FOREIGN KEY (KID) REFERENCES kunde(KID),
    FOREIGN KEY (MID) REFERENCES maschine(MID),
    CHECK (start < ende)
);

CREATE TABLE vermietung_zubehor (
    ZID INT,
    VID INT,
    menge INT,
    PRIMARY KEY (ZID, VID),
    FOREIGN KEY (ZID) REFERENCES zubehor(ZID),
    FOREIGN KEY (VID) REFERENCES vermietung(VID)
);
