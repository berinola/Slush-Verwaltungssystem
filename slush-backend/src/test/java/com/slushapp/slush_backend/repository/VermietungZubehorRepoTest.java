package com.slushapp.slush_backend.repository;

import com.slushapp.slush_backend.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.Month;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VermietungZubehorRepoTest {

    @Autowired
    private VermietungZubehorRepository vermietungZubehorRepository;
    @Autowired
    private VermietungRepository vermietungRepository;
    @Autowired
    private ZubehorRepository zubehorRepository;
    @Autowired
    private KundeRepository kundeRepository;
    @Autowired
    private MaschineRepository maschineRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update"); // oder create-drop
    }

    @Test
    public void found() {
        Kunde k = new Kunde();
        k.setFname("A");
        k.setLname("B");
        k.setMail("a@b.de");
        k.setTelefon("1");
        k = kundeRepository.save(k);

        Maschine m = new Maschine();
        m.setModell("X");
        m.setAnzahlTanks(1);
        m = maschineRepository.save(m);

        Vermietung v = new Vermietung();
        v.setStart(LocalDateTime.of(2025, Month.SEPTEMBER, 1, 8, 10));
        v.setEnde(LocalDateTime.of(2025, Month.SEPTEMBER, 2, 8, 10));
        v.setKunde(k);
        v.setMaschine(m);
        v = vermietungRepository.save(v);

        Zubehor z = new Zubehor();
        z.setAnzahl(3);
        z.setName("Becher");
        z.setPreis(8.5);
        z.setTyp("0.2");
        z = zubehorRepository.save(z);

        VermietungZubehorId id = new VermietungZubehorId(v.getVid(), z.getZid());

        VermietungZubehor vz = new VermietungZubehor();
        vz.setId(id);
        vz.setVermietung(v);
        vz.setZubehor(z);
        vermietungZubehorRepository.save(vz);

        var found = vermietungZubehorRepository.findById(id);
        assertThat(found).isPresent();
        assertThat(found.get().getVermietung().getVid()).isEqualTo(v.getVid());
        assertThat(found.get().getZubehor().getZid()).isEqualTo(z.getZid());
    }
}