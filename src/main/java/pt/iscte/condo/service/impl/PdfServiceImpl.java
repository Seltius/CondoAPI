package pt.iscte.condo.service.impl;

import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.filter.text.TextReplacerFilter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pt.iscte.condo.repository.entities.Condominium;
import pt.iscte.condo.repository.entities.Meeting;
import pt.iscte.condo.repository.entities.MeetingTopic;
import pt.iscte.condo.service.PdfService;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {
    @Override
    public byte[] generateMinute(List<MeetingTopic> topics, Map<String, String> meetingTopicsDescription, Condominium condominium, Meeting meeting) throws Exception {
        String topicsConcat = "";
        String topicsDescriptionConcat = "";

        for (MeetingTopic topic : topics) {
            topicsConcat += topic.getTopic() + "\n";
        }

        for (Map.Entry<String, String> entry : meetingTopicsDescription.entrySet()) {
            topicsDescriptionConcat += entry.getValue() + "\n";
        }

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
        Map<String, String> replacements = getStringStringMap(condominium, meeting, topicsConcat, topicsDescriptionConcat);

        // Create a new TextReplacerFilter
        TextReplacerFilter textReplacerFilter = new TextReplacerFilter(
                replacements.keySet().toArray(new String[0]),
                replacements.values().toArray(new String[0])
        );

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

    private static Map<String, String> getStringStringMap(Condominium condominium, Meeting meeting, String topics, String topicsDescription) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("condominiumName", condominium.getName());
        replacements.put("condominiumAddress", condominium.getAddress());
        replacements.put("meetingDay", String.valueOf(meeting.getDate().getDayOfMonth()));
        replacements.put("meetingHour", String.valueOf(meeting.getStartTime().getHour()));
        replacements.put("condominiumParish", condominium.getParish());
        replacements.put("condominiumCounty", condominium.getCountry()); //todo county instead of country
        replacements.put("meetingLink", meeting.getLink());
        replacements.put("meetingTopics", topics);
        replacements.put("meetingOrganizer", meeting.getOrganizer().getName());
        replacements.put("meetingSecretary", meeting.getSecretary().getName());
        replacements.put("meetingAiTopicsDescription", topicsDescription);
        return replacements;
    }
}