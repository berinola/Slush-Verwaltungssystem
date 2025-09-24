package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.entities.Maschine;
import com.slushapp.slush_backend.repository.MaschineRepository;
import com.slushapp.slush_backend.repository.VermietungRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaschineServiceTest {

    private MaschineService maschineService;
    private AutoCloseable closeable;

    @Mock
    private MaschineRepository maschineRepository;

    @Mock
    private VermietungRepository vermietungRepository;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        maschineService = new MaschineService(maschineRepository, vermietungRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    void getMaschines() {
        when(maschineRepository.findAll()).thenReturn(List.of(new Maschine(), new Maschine()));

        List<Maschine> result = maschineService.getMaschines();

        assertThat(result).hasSize(2);
        verify(maschineRepository).findAll();
    }

    @Test
    void getMaschine() {
        Maschine m = new Maschine();
        when(maschineRepository.findById(42L)).thenReturn(Optional.of(m));

        Maschine result = maschineService.getMaschine(42L);

        assertThat(result).isSameAs(m);
        verify(maschineRepository).findById(42L);
    }

    @Test
    void getMaschine_notFound() {
        when(maschineRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> maschineService.getMaschine(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maschine mit id 99 nicht gefunden");

        verify(maschineRepository).findById(99L);
    }

    @Test
    void createMaschine_validTanksAndModel() {
        Maschine m = new Maschine();
        m.setModell("Bagger 3000");
        m.setAnzahlTanks(2);

        when(maschineRepository.save(m)).thenReturn(m);

        Maschine result = maschineService.createMaschine(m);

        assertThat(result).isSameAs(m);
        ArgumentCaptor<Maschine> captor = ArgumentCaptor.forClass(Maschine.class);
        verify(maschineRepository).save(captor.capture());
        assertThat(captor.getValue().getModell()).isEqualTo("Bagger 3000");
        assertThat(captor.getValue().getAnzahlTanks()).isEqualTo(2);
    }


    @Test
    void createMaschine_invalid_tankAnzahl() {
        Maschine m = new Maschine();
        m.setModell("Bagger 3000");
        m.setAnzahlTanks(4);

        assertThatThrownBy(() -> maschineService.createMaschine(m))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nur 1-3 Tanks möglich");

        verify(maschineRepository, never()).save(any());
    }


    @Test
    void deleteMaschine_notFound() {
        when(maschineRepository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> maschineService.deleteMaschine(7L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Maschine mit id=7 existiert nicht");

        verify(maschineRepository).findById(7L);
        verify(vermietungRepository, never()).countByMaschine_Mid(anyLong());
        verify(maschineRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteMaschine() {
        Maschine m = new Maschine();
        when(maschineRepository.findById(3L)).thenReturn(Optional.of(m));
        when(vermietungRepository.countByMaschine_Mid(3L)).thenReturn(0L);

        Maschine deleted = maschineService.deleteMaschine(3L);

        assertThat(deleted).isSameAs(m);
        verify(maschineRepository).findById(3L);
        verify(vermietungRepository).countByMaschine_Mid(3L);
        verify(maschineRepository).deleteById(3L);
    }
}

