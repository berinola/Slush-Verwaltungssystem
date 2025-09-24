package com.slushapp.slush_backend.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VermietungZubehorId implements Serializable {
    private Long vid;
    private Long zid;

    // --- Constructors ---
    public VermietungZubehorId() {}

    public VermietungZubehorId(Long vid, Long zid) {
        this.vid = vid;
        this.zid = zid;
    }

    // --- equals & hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VermietungZubehorId)) return false;
        VermietungZubehorId that = (VermietungZubehorId) o;
        return Objects.equals(vid, that.vid) && Objects.equals(zid, that.zid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vid, zid);
    }

    // --- Getters & Setters ---
    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public Long getZid() {
        return zid;
    }

    public void setZid(Long zid) {
        this.zid = zid;
    }
}
