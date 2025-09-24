package com.slushapp.slush_backend.repository;

import com.slushapp.slush_backend.entities.Kunde;
import com.slushapp.slush_backend.entities.Vermietung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VermietungRepository extends JpaRepository<Vermietung, Long> {
    @Query("""
    SELECT COUNT(v) > 0
    FROM Vermietung v
    WHERE v.maschine.mid = :mid
      AND v.start < :ende
      AND v.ende   > :start
""")
    boolean existsOverlap(
            @Param("mid") Long maschineId,
            @Param("start") LocalDateTime start,
            @Param("ende") LocalDateTime ende
    );

    @Query("""
SELECT v
FROM Vermietung v
WHERE v.start >= :start
AND v.ende <= :ende
""")
    List<Vermietung> findByStart(
            @Param("start") LocalDateTime start,
            @Param("ende") LocalDateTime ende
    );


    List<Vermietung> getVermietungByKunde(Kunde kunde);

    long countByMaschine_Mid(Long id);
}
