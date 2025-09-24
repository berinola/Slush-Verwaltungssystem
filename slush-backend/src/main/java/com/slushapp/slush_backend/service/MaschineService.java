package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.entities.Maschine;
import com.slushapp.slush_backend.repository.MaschineRepository;
import com.slushapp.slush_backend.repository.VermietungRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaschineService {

    private final MaschineRepository maschineRepository;
    private final VermietungRepository vermietungRepository;

    public MaschineService(MaschineRepository maschineRepository, VermietungRepository vermietungRepository) {
        this.maschineRepository = maschineRepository;
        this.vermietungRepository = vermietungRepository;
    }

    public List<Maschine> getMaschines() {
        return maschineRepository.findAll();
    }

    public Maschine getMaschine(Long id) {
        Optional<Maschine> maschine = maschineRepository.findById(id);
        if (maschine.isPresent()) {
            return maschine.get();
        }
        throw new IllegalArgumentException("Maschine mit id " + id + " nicht gefunden");
    }

    public Maschine createMaschine(Maschine maschine) {
        if(maschine.getAnzahlTanks()>0 && maschine.getAnzahlTanks()<=3){
            maschineRepository.save(maschine);
            return maschine;
        }else if (maschine.getModell() == null || maschine.getModell().isBlank()){
            throw new IllegalArgumentException("Modell ist Pflicht");
        }
        throw new IllegalArgumentException("Nur 1-3 Tanks möglich");

    }

    public Maschine deleteMaschine(Long id) {
        Optional<Maschine> maschine = maschineRepository.findById(id);
        if (!maschine.isPresent()) {
            throw new IllegalArgumentException("Maschine mit id=" + id + " existiert nicht");
        }
        long refs = vermietungRepository.countByMaschine_Mid(id);
        if (refs > 0) {
            throw new IllegalStateException("Maschine kann nicht gelöscht werden: " + refs +
                    " Vermietung(en) referenzieren sie noch.");
        }
        Maschine m = maschine.get();
        maschineRepository.deleteById(id);
        return m;
    }




}
