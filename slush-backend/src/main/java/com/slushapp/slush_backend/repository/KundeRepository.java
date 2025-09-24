package com.slushapp.slush_backend.repository;

import com.slushapp.slush_backend.entities.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KundeRepository extends JpaRepository<Kunde, Long> {
}
