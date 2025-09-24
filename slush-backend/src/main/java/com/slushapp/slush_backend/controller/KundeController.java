package com.slushapp.slush_backend.controller;


import com.slushapp.slush_backend.service.KundeService;
import com.slushapp.slush_backend.entities.Kunde;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/kunden")
public class KundeController {
    private final KundeService kundeService;

    public KundeController(KundeService kundeService) {
        this.kundeService = kundeService;
    }

    @PostMapping
    public ResponseEntity<?> saveKunde(@RequestBody Kunde kunde) {
        try {
            return ResponseEntity.status(201).body(kundeService.saveKunde(kunde));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateKunde(@RequestBody Kunde kunde) {
        try {
            return ResponseEntity.ok(kundeService.updateKunde(kunde));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Kunde>> findAll() {
        return ResponseEntity.ok(kundeService.findAll());
    }

    @GetMapping("/{kundeId}")
    public ResponseEntity<Kunde> getKundeWithID(@PathVariable Long kundeId) {
        try {
            return ResponseEntity.ok(kundeService.getKundeById(kundeId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{kundeId}")
    public ResponseEntity<?> deleteKunde(@PathVariable Long kundeId) {
        try {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
