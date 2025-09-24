package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.entities.Kunde;
import com.slushapp.slush_backend.repository.KundeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KundeService {
    private KundeRepository kundeRepository;

    public KundeService(KundeRepository kundeRepository) {
        this.kundeRepository = kundeRepository;
    }

    public List<Kunde> findAll() {
        return kundeRepository.findAll();
    }

    public Kunde saveKunde(Kunde kunde) {
        if (kunde.getMail() == null || !kunde.getMail().contains("@") || !kunde.getMail().contains(".")) {
            throw new IllegalArgumentException("Invalid mail");
        } else if (kunde.getFname() == null || kunde.getLname() == null || kunde.getTelefon() == null) {
            throw new IllegalArgumentException("Alle Felder ausfüllen");
        }
        return kundeRepository.save(kunde);
    }

    public Kunde updateKunde(Kunde kunde) {
        Optional<Kunde> k = kundeRepository.findById(kunde.getKid());
        if (k.isPresent()) {
            return kundeRepository.save(kunde);
        }
        throw new IllegalArgumentException("Kunde nicht gefunden");
    }

    public Kunde getKundeById(Long kundeId) {
        Optional<Kunde> kunde = kundeRepository.findById(kundeId);
        if (kunde.isPresent()) {
            return kunde.get();
        }
        throw new IllegalArgumentException("Kunde nicht gefunden");
    }

    public Kunde deleteKunde(Long kundeId) {
        Optional<Kunde> kunde = kundeRepository.findById(kundeId);
        if (kunde.isPresent()) {
            kundeRepository.delete(kunde.get());
            return kunde.get();
        }
        throw new IllegalArgumentException("Kunde nicht gefunden");
    }


}
