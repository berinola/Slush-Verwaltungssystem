package com.slushapp.slush_backend.repository;

import com.slushapp.slush_backend.entities.VermietungZubehor;
import com.slushapp.slush_backend.entities.VermietungZubehorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VermietungZubehorRepository extends JpaRepository<VermietungZubehor, VermietungZubehorId> {
    @Query("""
    SELECT vz
    FROM VermietungZubehor vz
    WHERE vz.vermietung.vid = :vid
""")
    List<VermietungZubehor> findByVermietungId(@Param("vid") Long vid);
}
