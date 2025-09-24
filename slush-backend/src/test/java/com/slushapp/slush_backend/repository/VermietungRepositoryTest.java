package com.slushapp.slush_backend.repository;

import com.slushapp.slush_backend.entities.Kunde;
import com.slushapp.slush_backend.entities.Maschine;
import com.slushapp.slush_backend.entities.Vermietung;
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
public class VermietungRepositoryTest {

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
    @Autowired
    private VermietungRepository vermietungRepository;
    @Autowired
    private MaschineRepository maschineRepository;
    @Autowired
    private KundeRepository kundeRepository;

    @Test
    public void mieteOverlapp(){
        Maschine m = new Maschine();
        m.setModell("Slushomatic X");
        m.setAnzahlTanks(1);
        m = maschineRepository.save(m);

        Kunde k = new Kunde();
        k.setFname("Ali"); k.setLname("M");
        k.setMail("ali@test.de"); k.setTelefon("1");
        k = kundeRepository.save(k);

        Vermietung v = new Vermietung();
        v.setMaschine(m); v.setKunde(k);
        v.setStart(LocalDateTime.of(2025, Month.SEPTEMBER, 1,8,10));
        v.setEnde  (LocalDateTime.of(2025, Month.SEPTEMBER, 2,8,10));
        vermietungRepository.save(v);

        boolean overlapp = vermietungRepository.existsOverlap(m.getMid(),
                LocalDateTime.of(2025, Month.SEPTEMBER, 1,8,10),
                LocalDateTime.of(2025, Month.SEPTEMBER, 1,12,10)
        );

        assertThat(overlapp).isTrue();
    }

    @Test
    public void mieteNoOverlap(){
        Maschine m = new Maschine();
        m.setModell("Slushomatic X");
        m.setAnzahlTanks(1);
        m = maschineRepository.save(m);

        Kunde k = new Kunde();
        k.setFname("Ali"); k.setLname("M");
        k.setMail("ali@test.de"); k.setTelefon("1");
        k = kundeRepository.save(k);

        Vermietung v = new Vermietung();
        v.setMaschine(m); v.setKunde(k);
        v.setStart(LocalDateTime.of(2025, Month.SEPTEMBER, 1,8,10));
        v.setEnde  (LocalDateTime.of(2025, Month.SEPTEMBER, 2,8,10));
        vermietungRepository.save(v);

        boolean overlapp = vermietungRepository.existsOverlap(m.getMid(),
                LocalDateTime.of(2025, Month.SEPTEMBER, 3,8,10),
                LocalDateTime.of(2025, Month.SEPTEMBER, 4,12,10)
        );

        assertThat(overlapp).isFalse();
    }

    @Test
    public void mieteNoOverlapEdge(){
        Maschine m = new Maschine();
        m.setModell("Slushomatic X");
        m.setAnzahlTanks(1);
        m = maschineRepository.save(m);

        Kunde k = new Kunde();
        k.setFname("Ali"); k.setLname("M");
        k.setMail("ali@test.de"); k.setTelefon("1");
        k = kundeRepository.save(k);

        Vermietung v = new Vermietung();
        v.setMaschine(m); v.setKunde(k);
        v.setStart(LocalDateTime.of(2025, Month.SEPTEMBER, 1,8,10));
        v.setEnde  (LocalDateTime.of(2025, Month.SEPTEMBER, 2,8,10));
        vermietungRepository.save(v);

        boolean overlapp = vermietungRepository.existsOverlap(m.getMid(),
                LocalDateTime.of(2025, Month.SEPTEMBER, 2,8,10),
                LocalDateTime.of(2025, Month.SEPTEMBER, 4,12,10)
        );

        assertThat(overlapp).isFalse();
    }

    @Test
    public void GleichesIntervall_overlap() {
        Maschine m = new Maschine();
        m.setModell("X");
        m.setAnzahlTanks(1);
        m = maschineRepository.save(m);

        Kunde k = new Kunde();
        k.setFname("A");
        k.setLname("B");
        k.setMail("a@b.de");
        k.setTelefon("1");
        k = kundeRepository.save(k);

        LocalDateTime s = LocalDateTime.of(2025, 9, 1, 8, 10);
        LocalDateTime e = LocalDateTime.of(2025, 9, 2, 8, 10);

        Vermietung v = new Vermietung();
        v.setMaschine(m); v.setKunde(k);
        v.setStart(s);
        v.setEnde(e);
        vermietungRepository.save(v);

        boolean overlap = vermietungRepository.existsOverlap(m.getMid(), s, e);

        assertThat(overlap).isTrue();
    }


}
