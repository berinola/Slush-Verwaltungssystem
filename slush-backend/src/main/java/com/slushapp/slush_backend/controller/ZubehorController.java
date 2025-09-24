package com.slushapp.slush_backend.controller;

import com.slushapp.slush_backend.entities.Vermietung;
import com.slushapp.slush_backend.entities.VermietungZubehor;
import com.slushapp.slush_backend.entities.VermietungZubehorId;
import com.slushapp.slush_backend.entities.Zubehor;
import com.slushapp.slush_backend.repository.VermietungRepository;
import com.slushapp.slush_backend.repository.VermietungZubehorRepository;
import com.slushapp.slush_backend.repository.ZubehorRepository;
import com.slushapp.slush_backend.service.ZubehorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/zubehor")
public class ZubehorController {

    ZubehorService zubehorService;
    public ZubehorController(ZubehorService zubehorService) {
        this.zubehorService= zubehorService;
    }
    @GetMapping
    public ResponseEntity<?> getBestand(){
        return ResponseEntity.ok(zubehorService.getBestand());
    }

    @GetMapping("/{zubehorId}")
    public ResponseEntity<?> getZubehorById(@PathVariable Long zubehorId){
        try{
            return ResponseEntity.ok(zubehorService.getZubehor(zubehorId));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> saveZubehor(@RequestBody Zubehor zubehor){
        try {
            return ResponseEntity.ok(zubehorService.saveZubehor(zubehor));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{zubehorId}")
    public ResponseEntity<?> updateBestand(@PathVariable Long zubehorId,@RequestParam Integer anzahl){
        try {
            return ResponseEntity.ok(zubehorService.updateBestand(zubehorId, anzahl));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
