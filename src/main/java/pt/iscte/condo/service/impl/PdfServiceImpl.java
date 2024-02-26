package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import pt.iscte.condo.controller.dto.Topic;
import pt.iscte.condo.repository.entities.Condominium;
import pt.iscte.condo.repository.entities.Meeting;
import pt.iscte.condo.service.PdfService;
import pt.iscte.condo.service.dto.FractionInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    public byte[] generateMinute(List<Topic> topics, List<FractionInfo> fractionInfos, Map<String, String> aiSummarizedTopics, Condominium condominium, Meeting meeting) throws Exception {
        byte[] pdfBytes;
        String topicsDescriptionConcat = "";

        for (Map.Entry<String, String> entry : aiSummarizedTopics.entrySet()) {
            topicsDescriptionConcat += entry.getValue() + "\n";
        }

        // Prepare data model
        Map<String, Object> data = new HashMap<>(getReplacements(condominium, meeting, topicsDescriptionConcat));
        data.put("topics", topics);
        data.put("fractions", fractionInfos);

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariables(data);
        String document = templateEngine.process("minute", context);

        // Write workbook to byte array
        try {
            pdfBytes = generatePdfFromHtml(document);
        } catch (IOException e) {
            throw new Exception("Error writing PDF to byte array", e);
        }
        return pdfBytes;

    }

    private byte[] generatePdfFromHtml(String html) throws Exception {
        File tempFile = File.createTempFile("generated_pdf_", ".pdf");
        tempFile.deleteOnExit();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        // Render to the temporary file
        OutputStream outputStream = new FileOutputStream(tempFile);
        renderer.createPDF(outputStream);
        outputStream.close();

        // Read the temporary file into a byte array
        byte[] pdfBytes = Files.readAllBytes(tempFile.toPath());

        // Delete the temporary file
        tempFile.delete();
        outputStream.close();

        return pdfBytes;
    }

    private static Map<String, String> getReplacements(Condominium condominium, Meeting meeting, String topicsDescription) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("condominiumName", condominium.getName());
        replacements.put("condominiumAddress", condominium.getAddress());
        replacements.put("meetingDay", String.valueOf(meeting.getDate().getDayOfMonth()));
        replacements.put("meetingHour", String.valueOf(meeting.getStartTime().getHour()));
        replacements.put("condominiumParish", condominium.getParish());
        replacements.put("condominiumCounty", condominium.getCountry()); //todo county instead of country
        replacements.put("meetingLink", meeting.getLink());
        replacements.put("meetingOrganizer", meeting.getOrganizer().getName());
        replacements.put("meetingSecretary", meeting.getSecretary().getName());
        replacements.put("meetingAiTopicsDescription", topicsDescription);
        return replacements;
    }

}
