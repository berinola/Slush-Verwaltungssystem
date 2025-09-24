package com.slushapp.slush_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Kunde {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kid;

    private String lname;
    private String fname;
    private String mail;
    private String telefon;

    public Long getKid() {
        return kid;
    }

    public String getLname() {
        return lname;
    }

    public String getFname() {
        return fname;
    }

    public String getMail() {
        return mail;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setKid(Long kid) {
        this.kid = kid;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}
