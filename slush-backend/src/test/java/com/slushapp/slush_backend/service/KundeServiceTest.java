package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.entities.Kunde;
import com.slushapp.slush_backend.repository.KundeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KundeServiceTest {

    private KundeService kundeService;
    private AutoCloseable closeable;
    @Mock
    private KundeRepository kundeRepository;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        kundeService = new KundeService(kundeRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void saveKunde() {
        //given
        Kunde kunde = new Kunde();
        kunde.setFname("Hans");
        kunde.setLname("Hans");
        kunde.setMail("aaaa.d@gmail.com");
        kunde.setTelefon("12345");

        //when
        kundeService.saveKunde(kunde);

        //then
        ArgumentCaptor<Kunde> captor = ArgumentCaptor.forClass(Kunde.class);
        verify(kundeRepository).save(captor.capture());
        assertThat(captor.getValue().getKid()).isEqualTo(kunde.getKid());

    }

    @Test
    void saveKunde_Exception() {
        Kunde kunde = new Kunde();
        kunde.setFname("Hans");
        kunde.setLname("Hans");
        kunde.setMail("aaaa.dgmail.com");
        kunde.setTelefon("12345");
        assertThatThrownBy(() -> kundeService.saveKunde(kunde)).hasMessageContaining("Invalid mail");
        verify(kundeRepository, never()).save(any());
    }

    @Test
    void updateKunde_found() {
        Kunde k = new Kunde();
        k.setKid(1L);
        k.setMail("a@b.de");
        k.setFname("A");
        k.setLname("B");
        k.setTelefon("123");
        when(kundeRepository.findById(1L)).thenReturn(Optional.of(k));
        when(kundeRepository.save(k)).thenReturn(k);

        Kunde res = kundeService.updateKunde(k);

        assertThat(res).isNotNull();
        verify(kundeRepository).findById(1L);
        verify(kundeRepository).save(k);
    }

    @Test
    void updateKunde_notFound() {
        Kunde k = new Kunde();
        k.setKid(99L);
        when(kundeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> kundeService.updateKunde(k))
                .hasMessageContaining("Kunde nicht gefunden");
        verify(kundeRepository, never()).save(any());
    }

    @Test
    void findAllKunde() {
        when(kundeRepository.findAll()).thenReturn(List.of(new Kunde(), new Kunde()));
        assertThat(kundeService.findAll()).hasSize(2);
        verify(kundeRepository).findAll();
    }
}