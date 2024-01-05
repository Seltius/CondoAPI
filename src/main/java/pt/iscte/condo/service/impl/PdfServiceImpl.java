package pt.iscte.condo.service.impl;

import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.filter.text.TextReplacerFilter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pt.iscte.condo.service.PdfService;

import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class PdfServiceImpl implements PdfService {
    @Override
    public byte[] generateMinute(Map<String, String> data) throws Exception {

        // Load document template
        File file = new ClassPathResource("templates/minute.odt").getFile();

        // Convert to PDF
        File output = File.createTempFile("minute", ".pdf");

        // Start an office manager
        LocalOfficeManager officeManager = LocalOfficeManager.builder()
                .officeHome("C:\\Program Files (x86)\\OpenOffice 4")
                .install()
                .build();

        // Create arrays for search and replacement strings
        String[] searchList = new String[data.size()];
        String[] replacementList = new String[data.size()];

        // Fill the arrays with data from the map
        Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
        IntStream.range(0, data.size()).forEach(i -> {
            Map.Entry<String, String> entry = iterator.next();
            searchList[i] = entry.getKey();
            replacementList[i] = entry.getValue();
        });

        // Create a new TextReplacerFilter
        TextReplacerFilter textReplacerFilter = new TextReplacerFilter(searchList, replacementList);

        try {
            officeManager.start();

            LocalConverter converter = LocalConverter.builder()
                    .officeManager(officeManager)
                    .filterChain(textReplacerFilter)
                    .build();

            // Convert
            converter.convert(file)
                    .as(DefaultDocumentFormatRegistry.ODT)
                    .to(output)
                    .as(DefaultDocumentFormatRegistry.PDF)
                    .execute();

        } finally {
            // Stop the office manager
            officeManager.stop();
        }

        byte[] fileContent = Files.readAllBytes(output.toPath());
        output.delete();

        return fileContent;
    }
}