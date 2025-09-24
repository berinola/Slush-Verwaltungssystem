package com.slushapp.slush_backend.controller;

import com.slushapp.slush_backend.entities.Maschine;
import com.slushapp.slush_backend.repository.MaschineRepository;
import com.slushapp.slush_backend.service.MaschineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/maschine")
public class MaschineController {
    private final MaschineService maschineService;
    public MaschineController(MaschineService maschineService) {
        this.maschineService = maschineService;
    }

    @GetMapping
    public ResponseEntity<?> getMaschines() {
        return ResponseEntity.ok(maschineService.getMaschines());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getMaschine(@PathVariable Long id) {
        try{
            return  ResponseEntity.ok(maschineService.getMaschine(id));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createMaschine(@RequestBody Maschine maschine) {
        try {
            return ResponseEntity.status(201).body(maschineService.createMaschine(maschine));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteMaschine(@PathVariable Long id) {
        try {

            maschineService.deleteMaschine(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
