package pt.iscte.condo.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pt.iscte.condo.service.PdfService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {
    @Override
    public byte[] generateMinute(Map<String, String> replacements) throws Exception {

        // Load word document template
        File file = new ClassPathResource("templates/minute.docx").getFile();
        XWPFDocument document = new XWPFDocument(new FileInputStream(file));

        // Replace placeholders with data
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                for (Map.Entry<String, String> entry : replacements.entrySet()) {
                    text = text.replace(entry.getKey(), entry.getValue());
                }
                run.setText(text, 0);
            }
        }

        // Convert Word to PDF
        Document pdfDocument = new Document();
        File output = File.createTempFile("minute", ".pdf");
        PdfWriter.getInstance(pdfDocument, new FileOutputStream(output));
        pdfDocument.open();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            pdfDocument.add(new Paragraph(paragraph.getText()));
        }

        pdfDocument.close();
        byte[] fileContent = Files.readAllBytes(output.toPath());
        output.delete();

        return fileContent;
    }
}
