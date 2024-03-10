package pt.iscte.condo.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    public byte[] generateMinute(List<Topic> topics, List<FractionInfo> fractionInfos,
                                 Map<String, String> aiSummarizedTopics,
                                 Condominium condominium,
                                 Meeting meeting,
                                 Map<String, byte[]> attachments) throws Exception {

        // Prepare document variables
        List<String> topicsDescription = Arrays.asList(aiSummarizedTopics.values().toArray(new String[0]));
        List<String> attachmentNames = new ArrayList<>(attachments.keySet());
        List<byte[]> files = new ArrayList<>(attachments.values());

        Map<String, Object> data = new HashMap<>(
                getMinuteReplacements(condominium, meeting, topics, topicsDescription, fractionInfos, attachmentNames)
        );

        return generateDocumentFromTemplate("minute", data, files);

    }

    public byte[] generateMeetingNotice(Condominium condominium, Meeting meeting, List<Topic> topics, Map<String, byte[]> attachments) throws Exception {
        List<String> attachmentNames = new ArrayList<>(attachments.keySet());
        List<byte[]> files = new ArrayList<>(attachments.values());

        Map<String, Object> data = new HashMap<>(
                generateMeetingNoticeReplacements(condominium, meeting, topics, attachmentNames)
        );

        return generateDocumentFromTemplate("meeting_notice", data, files);

    }

    private byte[] generateDocumentFromTemplate(String templateName, Map<String, Object> data, List<byte[]> attachments) throws Exception {
        byte[] pdfBytes;

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariables(data);
        String document = templateEngine.process(templateName, context);

        // Write workbook to byte array
        try {
            pdfBytes = convert2Pdf(document, attachments);
        } catch (IOException e) {
            throw new Exception("Error writing PDF to byte array", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pdfBytes;
    }

    private byte[] convert2Pdf(String html, List<byte[]> attachments) throws Exception {
        File tempFile = File.createTempFile("generated_pdf_", ".pdf");
        tempFile.deleteOnExit();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        // Render to the temporary file
        OutputStream outputStream = new FileOutputStream(tempFile);
        renderer.createPDF(outputStream);
        outputStream.close();

        // Create a PdfReader instance for the original PDF
        PdfReader originalPdfReader = new PdfReader(Files.readAllBytes(tempFile.toPath()));

        // Create a Document and a PdfCopy
        Document document = new Document();
        outputStream = new FileOutputStream(tempFile);
        PdfCopy copy = new PdfCopy(document, outputStream);

        // Open the Document
        document.open();

        // Loop through the pages of the original PDF and add them to the PdfCopy
        int n = originalPdfReader.getNumberOfPages();
        for (int i = 0; i < n; ) {
            copy.addPage(copy.getImportedPage(originalPdfReader, ++i));
        }

        // If extraPagesList is not null or empty, loop through each byte array and add the pages to the PdfCopy
        if (attachments != null && !attachments.isEmpty()) {
            for (int j = attachments.size() - 1; j >= 0; j--) {
                byte[] extraPages = attachments.get(j);
                PdfReader extraPagesReader = new PdfReader(extraPages);
                n = extraPagesReader.getNumberOfPages();
                for (int i = 0; i < n; ) {
                    copy.addPage(copy.getImportedPage(extraPagesReader, ++i));
                }
                extraPagesReader.close();
            }
        }

        // Close the Document and the PdfReader instances
        document.close();
        originalPdfReader.close();

        // Read the temporary file into a byte array
        byte[] pdfBytes = Files.readAllBytes(tempFile.toPath());

        // Delete the temporary file
        tempFile.delete();
        outputStream.close();

        return pdfBytes;
    }

    private Map<String, Object> getMinuteReplacements(Condominium condominium, Meeting meeting, List<Topic> topics,
                                                      List<String> topicsDescription, List<FractionInfo> fractionInfos,
                                                      List<String> attachmentsNames) {
        Map<String, Object> replacements = new HashMap<>();
        replacements.put("condominiumName", condominium.getName());
        replacements.put("condominiumAddress", condominium.getAddress());
        replacements.put("meetingDay", String.valueOf(meeting.getDate().getDayOfMonth()));
        replacements.put("meetingHour", String.valueOf(meeting.getStartTime().getHour()));
        replacements.put("condominiumParish", condominium.getParish());
        replacements.put("condominiumCounty", condominium.getCounty());
        replacements.put("meetingLink", meeting.getLink());
        replacements.put("meetingOrganizer", meeting.getOrganizer().getName());
        replacements.put("meetingSecretary", meeting.getSecretary().getName());
        replacements.put("topics", topics);
        replacements.put("topicsDescription", topicsDescription);
        replacements.put("fractions", fractionInfos);
        replacements.put("attachmentsNames", attachmentsNames);
        return replacements;
    }

    private Map<String, Object> generateMeetingNoticeReplacements(Condominium condominium, Meeting meeting,
                                                                  List<Topic> topics, List<String> attachmentsNames) {
        Locale ptPt = new Locale("pt", "PT");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", ptPt);
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Map<String, Object> replacements = new HashMap<>();
        replacements.put("condominiumName", condominium.getName());
        replacements.put("condominiumAddress", condominium.getAddress());
        replacements.put("condominiumCounty", condominium.getCounty());
        replacements.put("meetingDate", meeting.getDate().format(dateFormatter));
        replacements.put("meetingHour", meeting.getStartTime().format(hourFormatter));
        replacements.put("meetingLink", meeting.getLink());
        replacements.put("topics", topics);
        replacements.put("attachmentsNames", attachmentsNames);
        replacements.put("today", LocalDateTime.now().format(dateFormatter));

        if (meeting.getExtraMeetingDate() != null && meeting.getExtraStartTime() != null) {
            replacements.put("extraMeetingDate", meeting.getExtraMeetingDate().format(dateFormatter));
            replacements.put("extraMeetingHour", meeting.getExtraStartTime().format(hourFormatter));
        }

        return replacements;
    }

}
