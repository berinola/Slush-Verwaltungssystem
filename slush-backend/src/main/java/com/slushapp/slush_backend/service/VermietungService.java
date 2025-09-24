package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.controller.ZubehorController;
import com.slushapp.slush_backend.entities.*;
import com.slushapp.slush_backend.repository.KundeRepository;
import com.slushapp.slush_backend.repository.VermietungRepository;
import com.slushapp.slush_backend.repository.VermietungZubehorRepository;
import com.slushapp.slush_backend.repository.ZubehorRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VermietungService {
    private final VermietungRepository vermietungRepository;
    private final VermietungZubehorRepository vermietungZubehorRepository;
    private final ZubehorRepository zubehorRepository;
    private final ZubehorService zubehorService;
    private final KundeRepository kundeRepository;

    public VermietungService(VermietungRepository vermietungRepository, VermietungZubehorRepository vermietungZubehorRepository, ZubehorRepository zubehorRepository, ZubehorService zubehorService, KundeRepository kundeRepository) {
        this.vermietungRepository = vermietungRepository;
        this.vermietungZubehorRepository = vermietungZubehorRepository;
        this.zubehorRepository = zubehorRepository;
        this.zubehorService = zubehorService;
        this.kundeRepository = kundeRepository;
    }

    public List<Vermietung> allVermietung() {
        return vermietungRepository.findAll();
    }

    public Vermietung getVermietung(Long vermietungId) {
        Optional<Vermietung> vermietung = vermietungRepository.findById(vermietungId);
        if (vermietung.isPresent()) {
            return vermietung.get();
        }
        throw new IllegalArgumentException("Vermietung id '" + vermietungId + "' does not exist");
    }

    public List<VermietungZubehor> getZubehorForVermietung(Long vermietungId) {
        Optional<Vermietung> vermietung = vermietungRepository.findById(vermietungId);
        if (vermietung.isPresent()) {
            Vermietung v = vermietung.get();
            List<VermietungZubehor> zubehore = vermietungZubehorRepository.findByVermietungId(v.getVid());
            return zubehore;
        }
        throw new IllegalArgumentException("Vermietung id '" + vermietungId + "' does not exist");
    }

    public List<Vermietung> getVermietungByStart(LocalDateTime start, LocalDateTime ende) {
        return vermietungRepository.findByStart(start, ende);
    }

    private List<Zubehor> ZubehorList(List<VermietungZubehor> zubehore) {
        List<Zubehor> zubehorList = new ArrayList<>();
        for (VermietungZubehor vz : zubehore) {
            zubehorList.add(vz.getZubehor());
        }
        return zubehorList;
    }

    public Vermietung mieten(Vermietung vermietung) {
        Maschine m = vermietung.getMaschine();
        if (vermietungRepository.existsOverlap(m.getMid(), vermietung.getStart(), vermietung.getEnde())) {
            throw new InvalidParameterException("Overlap exists");
        }
        return vermietungRepository.save(vermietung);

    }

    public void postZubehorForVermietung(Long vermietungId, Long zubehorId, Integer menge) {
        if (menge <= 0) {
            throw new IllegalArgumentException("Menge muss > 0 sein");
        }
        Optional<Vermietung> vermietung = vermietungRepository.findById(vermietungId);
        Optional<Zubehor> zubehor = zubehorRepository.findById(zubehorId);
        if (vermietung.isPresent() && zubehor.isPresent()) {
            Vermietung v = vermietung.get();
            Zubehor z = zubehor.get();
            if (z.getAnzahl() < menge) {
                throw new IllegalStateException("Nicht genug Zubehör. Nur noch:" + z.getAnzahl() + " von " + z.getName());
            }
            VermietungZubehorId vzId = new VermietungZubehorId(v.getVid(), z.getZid());
            VermietungZubehor vz = new VermietungZubehor();
            vz.setId(vzId);
            vz.setVermietung(v);
            vz.setZubehor(z);
            vz.setMenge(menge);
            zubehorService.updateBestand(z.getZid(), -1 * menge);
            vermietungZubehorRepository.save(vz);
        } else {
            throw new IllegalArgumentException("Vermietung id or Zubehor id '" + vermietungId + "' does not exist");
        }
    }

    public List<Vermietung> getVermietungForKunde(Long kundeID) {
        Optional<Kunde> kunde = kundeRepository.findById(kundeID);
        if (kunde.isPresent()) {
            return vermietungRepository.getVermietungByKunde(kunde.get());
        }
        throw new IllegalArgumentException("Kunde id '" + kundeID + "' does not exist");

    }

    public double gesamtPreis(Long vermietungId) {
        double totalPreis = 0;
        List<VermietungZubehor> zubehore = this.getZubehorForVermietung(vermietungId);
        for (VermietungZubehor vz : zubehore) {
            totalPreis+= vz.getZubehor().getPreis()* vz.getMenge();
        }
        return totalPreis;

    }

}