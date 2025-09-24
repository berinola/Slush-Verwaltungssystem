package com.slushapp.slush_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Maschine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;
    private String modell;
    private Integer anzahlTanks;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public Integer getAnzahlTanks() {
        return anzahlTanks;
    }
    public void setAnzahlTanks(Integer anzahlTanks) {
        this.anzahlTanks = anzahlTanks;
    }
}
