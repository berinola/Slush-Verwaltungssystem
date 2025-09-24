package com.slushapp.slush_backend.repository;

import com.slushapp.slush_backend.entities.Maschine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaschineRepository extends JpaRepository<Maschine, Long> {
}
