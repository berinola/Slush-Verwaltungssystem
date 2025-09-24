package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.entities.*;
import com.slushapp.slush_backend.repository.KundeRepository;
import com.slushapp.slush_backend.repository.VermietungRepository;
import com.slushapp.slush_backend.repository.VermietungZubehorRepository;
import com.slushapp.slush_backend.repository.ZubehorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VermietungServiceTest {

    private VermietungService service;
    private AutoCloseable closeable;

    @Mock VermietungRepository vermietungRepository;
    @Mock VermietungZubehorRepository vermietungZubehorRepository;
    @Mock ZubehorRepository zubehorRepository;
    @Mock ZubehorService zubehorService;
    @Mock KundeRepository kundeRepository;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        service = new VermietungService(
                vermietungRepository,
                vermietungZubehorRepository,
                zubehorRepository,
                zubehorService,
                kundeRepository
        );
    }

    @AfterEach
    void tearDown() throws Exception { closeable.close(); }


    @Test
    void allVermietung() {
        when(vermietungRepository.findAll()).thenReturn(List.of(new Vermietung(), new Vermietung()));

        var result = service.allVermietung();

        assertThat(result).hasSize(2);
        verify(vermietungRepository).findAll();
    }

    @Test
    void getZubehorForVermietung() {
        Vermietung v = new Vermietung();
        v.setVid(5L);
        when(vermietungRepository.findById(5L)).thenReturn(Optional.of(v));
        when(vermietungZubehorRepository.findByVermietungId(5L))
                .thenReturn(List.of(new VermietungZubehor(), new VermietungZubehor()));

        var list = service.getZubehorForVermietung(5L);

        assertThat(list).hasSize(2);
        verify(vermietungZubehorRepository).findByVermietungId(5L);
    }

    @Test
    void getVermietungByStart() {
        LocalDateTime s = LocalDateTime.now();
        LocalDateTime e = s.plusDays(2);
        when(vermietungRepository.findByStart(s, e)).thenReturn(List.of(new Vermietung()));

        var result = service.getVermietungByStart(s, e);

        assertThat(result).hasSize(1);
        verify(vermietungRepository).findByStart(s, e);
    }

    @Test
    void mieten_overlap() {
        Maschine m = new Maschine(); m.setMid(9L);
        Vermietung v = new Vermietung();
        v.setMaschine(m);
        v.setStart(LocalDateTime.now());
        v.setEnde(LocalDateTime.now().plusDays(1));

        when(vermietungRepository.existsOverlap(eq(9L), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> service.mieten(v))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessageContaining("Overlap exists");

        verify(vermietungRepository, never()).save(any());
    }

    @Test
    void mieten_NoOverlap() {
        Maschine m = new Maschine(); m.setMid(9L);
        Vermietung v = new Vermietung();
        v.setMaschine(m);
        v.setStart(LocalDateTime.now());
        v.setEnde(LocalDateTime.now().plusDays(1));

        when(vermietungRepository.existsOverlap(eq(9L), any(), any())).thenReturn(false);
        when(vermietungRepository.save(v)).thenReturn(v);

        var saved = service.mieten(v);

        assertThat(saved).isSameAs(v);
        verify(vermietungRepository).save(v);
    }

    @Test
    void postZubehor_Pos() {
        assertThatThrownBy(() -> service.postZubehorForVermietung(1L, 2L, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Menge muss > 0 sein");
        verifyNoInteractions(vermietungRepository, zubehorRepository, zubehorService, vermietungZubehorRepository);
    }

    @Test
    void postZubehor_IllegalState() {
        Vermietung v = new Vermietung(); v.setVid(11L);
        Zubehor z = new Zubehor(); z.setZid(22L); z.setName("Schlauch"); z.setAnzahl(0);

        when(vermietungRepository.findById(11L)).thenReturn(Optional.of(v));
        when(zubehorRepository.findById(22L)).thenReturn(Optional.of(z));

        assertThatThrownBy(() -> service.postZubehorForVermietung(11L, 22L, 2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Nicht genug Zubehör")
                .hasMessageContaining("Schlauch");

        verify(vermietungZubehorRepository, never()).save(any());
        verify(zubehorService, never()).updateBestand(anyLong(), anyInt());
    }

    @Test
    void postZubehor_AktualisiertBestand() {
        Vermietung v = new Vermietung(); v.setVid(11L);
        Zubehor z = new Zubehor(); z.setZid(22L); z.setName("Schlauch"); z.setAnzahl(5);

        when(vermietungRepository.findById(11L)).thenReturn(Optional.of(v));
        when(zubehorRepository.findById(22L)).thenReturn(Optional.of(z));

        service.postZubehorForVermietung(11L, 22L, 3);


        verify(zubehorService).updateBestand(22L, -3);


        ArgumentCaptor<VermietungZubehor> captor = ArgumentCaptor.forClass(VermietungZubehor.class);
        verify(vermietungZubehorRepository).save(captor.capture());
        VermietungZubehor saved = captor.getValue();
        assertThat(saved.getVermietung().getVid()).isEqualTo(11L);
        assertThat(saved.getZubehor().getZid()).isEqualTo(22L);
        assertThat(saved.getMenge()).isEqualTo(3);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getId().getVid()).isEqualTo(11L);
        assertThat(saved.getId().getZid()).isEqualTo(22L);
    }

    @Test
    void getVermietungForKunde() {
        Kunde k = new Kunde(); k.setKid(7L);
        when(kundeRepository.findById(7L)).thenReturn(Optional.of(k));
        when(vermietungRepository.getVermietungByKunde(k)).thenReturn(List.of(new Vermietung()));

        var list = service.getVermietungForKunde(7L);

        assertThat(list).hasSize(1);
        verify(vermietungRepository).getVermietungByKunde(k);
    }

    @Test
    void gesamtPreis() {
        // Vermietung vorhanden
        Vermietung v = new Vermietung(); v.setVid(44L);
        when(vermietungRepository.findById(44L)).thenReturn(Optional.of(v));

        // Zubehör-Positionen
        Zubehor z1 = new Zubehor(); z1.setZid(1L); z1.setPreis(10.0);
        Zubehor z2 = new Zubehor(); z2.setZid(2L); z2.setPreis(2.5);

        VermietungZubehor vz1 = new VermietungZubehor();
        vz1.setId(new VermietungZubehorId(44L, 1L));
        vz1.setVermietung(v); vz1.setZubehor(z1); vz1.setMenge(3);

        VermietungZubehor vz2 = new VermietungZubehor();
        vz2.setId(new VermietungZubehorId(44L, 2L));
        vz2.setVermietung(v); vz2.setZubehor(z2); vz2.setMenge(4);

        when(vermietungZubehorRepository.findByVermietungId(44L)).thenReturn(List.of(vz1, vz2));

        double total = service.gesamtPreis(44L);

        assertThat(total).isEqualTo(40.0);
    }
}