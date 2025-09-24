package com.slushapp.slush_backend.service;

import com.slushapp.slush_backend.entities.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class InvoicePDFServiceTest {

    @Mock SpringTemplateEngine templateEngine;
    @Mock VermietungService vermietungService;

    private InvoicePDFService service;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        service = new InvoicePDFService(templateEngine, vermietungService);
    }

    @AfterEach
    void tearDown() throws Exception { closeable.close(); }

    @Test
    void renderForVermietung() {

        Kunde kunde = new Kunde(); kunde.setFname("Max");
        Maschine maschine = new Maschine(); maschine.setModell("Bagger 3000");
        Vermietung v = new Vermietung();
        v.setVid(1L);
        v.setKunde(kunde);
        v.setMaschine(maschine);
        v.setStart(LocalDateTime.of(2025, 1, 1, 9, 0));
        v.setEnde(LocalDateTime.of(2025, 1, 3, 18, 0));

        Zubehor z1 = new Zubehor(); z1.setZid(10L); z1.setName("Schlauch"); z1.setPreis(10.0);
        Zubehor z2 = new Zubehor(); z2.setZid(11L); z2.setName("Adapter");  z2.setPreis(2.5);

        VermietungZubehor vz1 = new VermietungZubehor();
        vz1.setId(new VermietungZubehorId(1L, 10L));
        vz1.setVermietung(v); vz1.setZubehor(z1); vz1.setMenge(3);

        VermietungZubehor vz2 = new VermietungZubehor();
        vz2.setId(new VermietungZubehorId(1L, 11L));
        vz2.setVermietung(v); vz2.setZubehor(z2); vz2.setMenge(4);

        when(vermietungService.getVermietung(1L)).thenReturn(v);
        when(vermietungService.getZubehorForVermietung(1L)).thenReturn(List.of(vz1, vz2));
        when(vermietungService.gesamtPreis(1L)).thenReturn(40.0);


        when(templateEngine.process(eq("invoice"), any(Context.class)))
                .thenReturn("<html><head><meta charset='utf-8'></head><body><h1>Rechnung</h1></body></html>");

        ArgumentCaptor<Context> ctxCaptor = ArgumentCaptor.forClass(Context.class);

        byte[] pdf = service.renderForVermietung(1L);

        assertThat(pdf).isNotNull();
        assertThat(pdf.length).isGreaterThan(100);

        verify(templateEngine).process(eq("invoice"), ctxCaptor.capture());
        Context ctx = ctxCaptor.getValue();
        Object invoiceObj = ctx.getVariable("invoice");
        assertThat(invoiceObj).isInstanceOf(Map.class);


        Map<String, Object> invoice = (Map<String, Object>) invoiceObj;

        assertThat(invoice.get("number")).isEqualTo("R-1");
        assertThat(invoice.get("customerName")).isEqualTo("Max");
        assertThat(invoice.get("machineName")).isEqualTo("Bagger 3000");
        assertThat(invoice.get("start")).isEqualTo(v.getStart());
        assertThat(invoice.get("end")).isEqualTo(v.getEnde());
        assertThat(invoice.get("gesamtPreis")).isEqualTo(40.0);
        assertThat(invoice.get("date")).isInstanceOf(LocalDate.class);
        assertThat(invoice.get("basePrice")).isEqualTo("100,00 €");


        List<Map<String, Object>> zubehorList = (List<Map<String, Object>>) invoice.get("zubehor");
        assertThat(zubehorList).hasSize(2);

        Map<String, Object> i1 = zubehorList.get(0);
        assertThat(i1.get("name")).isEqualTo("Schlauch");
        assertThat(i1.get("menge")).isEqualTo(3);
        assertThat(i1.get("preis")).isEqualTo(10.0);
        assertThat(i1.get("summe")).isEqualTo(30.0);

        Map<String, Object> i2 = zubehorList.get(1);
        assertThat(i2.get("name")).isEqualTo("Adapter");
        assertThat(i2.get("menge")).isEqualTo(4);
        assertThat(i2.get("preis")).isEqualTo(2.5);
        assertThat(i2.get("summe")).isEqualTo(10.0);


        verify(vermietungService).getVermietung(1L);
        verify(vermietungService).getZubehorForVermietung(1L);
        verify(vermietungService).gesamtPreis(1L);
    }
}
