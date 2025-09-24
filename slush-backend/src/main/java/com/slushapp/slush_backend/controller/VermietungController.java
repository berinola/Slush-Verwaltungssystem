package com.slushapp.slush_backend.controller;

import com.slushapp.slush_backend.entities.Maschine;
import com.slushapp.slush_backend.entities.Vermietung;
import com.slushapp.slush_backend.entities.VermietungZubehor;
import com.slushapp.slush_backend.repository.VermietungRepository;
import com.slushapp.slush_backend.repository.VermietungZubehorRepository;
import com.slushapp.slush_backend.service.InvoicePDFService;
import com.slushapp.slush_backend.service.VermietungService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("vermietung")
public class VermietungController {
    private final VermietungService vermietungService;
    private final InvoicePDFService invoicePDFService;

    public VermietungController(VermietungService vermietungService, InvoicePDFService invoicePDFService) {
        this.vermietungService = vermietungService;
        this.invoicePDFService = invoicePDFService;
    }

    @PostMapping
    public ResponseEntity<?> mieten(@RequestBody Vermietung mietung) {
        try {
            return ResponseEntity.status(201).body(vermietungService.mieten(mietung));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Vermietung>> allVermietungen() {
        return ResponseEntity.ok(vermietungService.allVermietung());
    }

    @GetMapping("/{vermietungId}")
    public ResponseEntity<?> vermietung(@PathVariable Long vermietungId) {
        try {
            return ResponseEntity.ok(vermietungService.getVermietung(vermietungId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{vermietungId}/zubehor")
    public ResponseEntity<?> getZubehorForVermietung(@PathVariable Long vermietungId) {
        try {
            return ResponseEntity.ok(vermietungService.getZubehorForVermietung(vermietungId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{start}/{ende}")
    public ResponseEntity<?> getZubehorForVermietung(@PathVariable LocalDateTime start, @PathVariable LocalDateTime ende) {
        try {
            return ResponseEntity.ok(vermietungService.getVermietungByStart(start, ende));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/kunde/{kundeID}")
    public ResponseEntity<?> getVermietungenForKunde(@PathVariable Long kundeID) {
        try {
            return ResponseEntity.ok(vermietungService.getVermietungForKunde(kundeID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{vermietungId}/zubehor/{zubehorId}")
    public ResponseEntity<?> postZubehorForVermietung(@PathVariable Long vermietungId, @PathVariable Long zubehorId, @RequestParam Integer menge) {
        try {
            vermietungService.postZubehorForVermietung(vermietungId, zubehorId, menge);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}/rechnung.pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> getRechnung(@PathVariable long id) {
        byte[] pdf = invoicePDFService.renderForVermietung(id);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=rechnung-" + id + ".pdf")
                .body(pdf);
    }


}
