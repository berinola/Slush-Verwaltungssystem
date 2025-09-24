package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.entities.VermietungZubehor;
import com.slushapp.slush_backend.entities.Zubehor;
import com.slushapp.slush_backend.repository.VermietungRepository;
import com.slushapp.slush_backend.repository.VermietungZubehorRepository;
import com.slushapp.slush_backend.repository.ZubehorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class ZubehorService {
    ZubehorRepository zubehorRepository;
    VermietungZubehorRepository vermietungZubehorRepository;
    VermietungRepository vermietungRepository;
    public ZubehorService(ZubehorRepository zubehorRepository, VermietungRepository vermietungRepository, VermietungZubehorRepository vermietungZubehorRepository) {
        this.zubehorRepository = zubehorRepository;
        this.vermietungRepository = vermietungRepository;
        this.vermietungZubehorRepository = vermietungZubehorRepository;
    }

    public List<Zubehor> getBestand(){
        return zubehorRepository.findAll();
    }

    public Zubehor getZubehor(Long zubehorId) {
        Optional<Zubehor> zubehor = zubehorRepository.findById(zubehorId);
        if(zubehor.isPresent()) {
            return zubehor.get();
        }
        throw new InvalidParameterException("Zubehor nicht gefunden");
    }

    public Zubehor saveZubehor(Zubehor zubehor) {
        if(zubehor.getAnzahl()>=0 && zubehor.getPreis()>=0){
        return zubehorRepository.save(zubehor);}
        throw new InvalidParameterException("Zubehor nicht gefunden");
    }

    public Zubehor updateBestand(Long zubehorId, Integer anzahl){
        Optional<Zubehor> zubehor = zubehorRepository.findById(zubehorId);
        if(zubehor.isPresent()) {
            Zubehor z = zubehor.get();
            if(z.getAnzahl()+anzahl>-1) {
                z.setAnzahl(z.getAnzahl() + anzahl);
                return zubehorRepository.save(z);
            }
        }
        throw new InvalidParameterException("Nicht genug Zubehör");
    }


}
