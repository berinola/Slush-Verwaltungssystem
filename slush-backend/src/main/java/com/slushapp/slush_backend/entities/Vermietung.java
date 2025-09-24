package com.slushapp.slush_backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Vermietung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vid;

    private LocalDateTime start;
    private LocalDateTime ende;

    @ManyToOne()
    @JoinColumn(name="kid", nullable=false)
    private Kunde kunde;

    @ManyToOne()
    @JoinColumn(name="mid", nullable=false)
    private Maschine maschine;

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnde() {
        return ende;
    }

    public void setEnde(LocalDateTime ende) {
        this.ende = ende;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public Maschine getMaschine() {
        return maschine;
    }

    public void setMaschine(Maschine maschine) {
        this.maschine = maschine;
    }
}
