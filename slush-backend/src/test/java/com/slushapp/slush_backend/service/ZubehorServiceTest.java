package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.entities.Zubehor;
import com.slushapp.slush_backend.repository.VermietungRepository;
import com.slushapp.slush_backend.repository.VermietungZubehorRepository;
import com.slushapp.slush_backend.repository.ZubehorRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ZubehorServiceTest {

    private ZubehorService service;
    private AutoCloseable closeable;

    @Mock ZubehorRepository zubehorRepository;
    @Mock VermietungRepository vermietungRepository;
    @Mock VermietungZubehorRepository vermietungZubehorRepository;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        service = new ZubehorService(zubehorRepository, vermietungRepository, vermietungZubehorRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getBestand() {
        when(zubehorRepository.findAll()).thenReturn(List.of(new Zubehor(), new Zubehor()));

        var result = service.getBestand();

        assertThat(result).hasSize(2);
        verify(zubehorRepository).findAll();
    }

    @Test
    void saveZubehor_negativerPreis() {
        Zubehor z = new Zubehor();
        z.setName("Schlauch");
        z.setAnzahl(1);
        z.setPreis(-0.01);

        assertThatThrownBy(() -> service.saveZubehor(z))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("Zubehor nicht gefunden");

        verify(zubehorRepository, never()).save(any());
    }

    @Test
    void updateBestand() {
        Zubehor z = new Zubehor();
        z.setZid(22L);
        z.setAnzahl(5);

        when(zubehorRepository.findById(22L)).thenReturn(Optional.of(z));
        when(zubehorRepository.save(any(Zubehor.class))).thenAnswer(inv -> inv.getArgument(0));

        var updated = service.updateBestand(22L, -2); // 5 + (-2) = 3 > 0

        assertThat(updated.getAnzahl()).isEqualTo(3);
        verify(zubehorRepository).save(z);
    }

    @Test
    void updateBestand_Negativ() {
        Zubehor z = new Zubehor();
        z.setZid(31L);
        z.setAnzahl(2);

        when(zubehorRepository.findById(31L)).thenReturn(Optional.of(z));

        assertThatThrownBy(() -> service.updateBestand(31L, -5))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("Nicht genug Zubehör");

        verify(zubehorRepository, never()).save(any());
    }
}
