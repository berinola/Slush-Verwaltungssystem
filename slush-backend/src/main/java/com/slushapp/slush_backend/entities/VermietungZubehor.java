package com.slushapp.slush_backend.entities;

import jakarta.persistence.*;

@Entity
public class VermietungZubehor {

    @EmbeddedId
    private VermietungZubehorId id; // composite key (VID + ZID)

    @ManyToOne
    @MapsId("vid") // maps part of the composite key
    @JoinColumn(name = "vid")
    private Vermietung vermietung;

    @ManyToOne
    @MapsId("zid")
    @JoinColumn(name = "zid")
    private Zubehor zubehor;

    private Integer menge;

    // --- Getters & Setters ---
    public VermietungZubehorId getId() {
        return id;
    }

    public void setId(VermietungZubehorId id) {
        this.id = id;
    }

    public Vermietung getVermietung() {
        return vermietung;
    }

    public void setVermietung(Vermietung vermietung) {
        this.vermietung = vermietung;
    }

    public Zubehor getZubehor() {
        return zubehor;
    }

    public void setZubehor(Zubehor zubehor) {
        this.zubehor = zubehor;
    }

    public Integer getMenge() {
        return menge;
    }

    public void setMenge(Integer menge) {
        this.menge = menge;
    }
}

