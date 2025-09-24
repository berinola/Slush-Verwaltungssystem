package com.slushapp.slush_backend.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.slushapp.slush_backend.service.VermietungService;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class InvoicePDFService {

    private final SpringTemplateEngine templateEngine;
    private final VermietungService vermietungService;

    public InvoicePDFService(SpringTemplateEngine templateEngine,
                             VermietungService vermietungService) {
        this.templateEngine = templateEngine;
        this.vermietungService = vermietungService;
    }

    public byte[] renderForVermietung(long vermietungId) {
        var v = vermietungService.getVermietung(vermietungId);
        var zubehor = vermietungService.getZubehorForVermietung(vermietungId);
        var gesamtPreis = vermietungService.gesamtPreis(vermietungId);

        var dto = new HashMap<String, Object>();
        dto.put("number", "R-" + vermietungId);
        dto.put("date", LocalDate.now());
        dto.put("customerName", v.getKunde().getFname());
        dto.put("machineName", v.getMaschine().getModell());
        dto.put("start", v.getStart());
        dto.put("end", v.getEnde());
        dto.put("basePrice", "100,00 €");
        dto.put("zubehor", zubehor.stream().map(vz -> Map.of(
                "name", vz.getZubehor().getName(),
                "menge", vz.getMenge(),
                "preis", vz.getZubehor().getPreis(),
                "summe", vz.getZubehor().getPreis() * vz.getMenge()
        )).toList());
        dto.put("gesamtPreis", gesamtPreis);

        var ctx = new Context(Locale.GERMANY);
        ctx.setVariable("invoice", dto);
        String html = templateEngine.process("invoice", ctx);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document jsoup = Jsoup.parse(html);
            jsoup.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

            org.w3c.dom.Document w3c = new W3CDom().fromJsoup(jsoup);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withW3cDocument(w3c, null);
            builder.toStream(out);
            builder.run();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Erstellen des PDFs", e);
        }
    }
}

