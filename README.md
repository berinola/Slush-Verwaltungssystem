# Slush-Verwaltungssystem

Ein Fullstack-Projekt (Spring Boot + React), das die Verwaltung von Slush-Maschinen, Zubehör,Kunden und Vermietungen ermöglicht.  
Das Projekt entstand privat, um **Fullstack-Entwicklung in der Praxis zu üben** und **einen kompletten Technologie-Stack** (Backend + Frontend + Datenbank) einzusetzen.

---

## Features
- **Backend (Spring Boot, Java 17)**  
  - REST-API für Kunden, Maschinen, Zubehör und Vermietungen  
  - Validierungen (z. B. E-Mail-Format, Anzahl Tanks, Bestandsprüfungen)  
  - Geschäftslogik: keine Doppelvermietungen, Zubehör nur bei ausreichendem Bestand  
  - Automatische Generierung von **PDF-Rechnungen** für Vermietungen über Thymeleaf & OpenHTMLtoPDF  
  - Unit-Tests mit JUnit & Mockito

- **Frontend (React + JavaScript)**  
  - CRUD-Views für Vermietungen, Kunden, Maschinen und Zubehör  
  - Vermietungsübersicht mit Start-/Enddatum, Name, Zubehör 
  - Einfache Validierung und Benutzerführung  
  - Moderne React-Komponenten (Hooks)

- **Datenbank (SQL / JPA)**  
  - Relationale Abbildung von Kunden, Maschinen, Zubehör und Vermietungen  
  - Die Datenbankzugriffe laufen über **Spring Data JPA** mit **Hibernate** als ORM-Framework

---

## Tech-Stack
- **Backend:** Spring Boot, Java 17, Spring Data JPA, Thymeleaf, JUnit, Mockito  
- **Frontend:** React, JavaScript, HTML/CSS  
- **Datenbank:** PostgreSQL 
- **Build Tools:** Maven (Backend), npm (Frontend)  

---

## Projektstruktur

Slush-VS/
├── slush-backend/ # Spring Boot Backend
│ ├── src/main/java/com/slushapp/slush_backend/
│ │ ├── controller # REST-Controller
│ │ ├── entities # JPA-Entities (ORM)
│ │ ├── repository # Spring Data JPA Repositories
│ │ └── service # Business-Logik
│ ├── src/main/resources/
│ │ ├── templates/invoice.html # Vorlage für Rechnungs-PDF
│ │ ├── application.properties
│ └── src/test/java/... # Unit-Tests
│
├── slush-frontend/ # React Frontend
│ ├── src/components # Wiederverwendbare UI-Komponenten
│ ├── src/pages # Views
│ ├── App.jsx, main.jsx # Einstiegspunkte
│ └── vite.config.js # Vite-Konfiguration
│
└── SQL/ # SQL-Skripte (z.B. Schema, Testdaten, Resetdaten)

## Installation & Start

### Backend starten
```bash
cd slush-backend
Projekt starten
cd läuft anschließend unter: http://localhost:8080
```
### Frontend starten
```bash
cd slush-frontend
npm install
npm run dev
Frontend läuft anschließend unter: http://localhost:5173
```
