package pt.iscte.condo.service;

import pt.iscte.condo.repository.entities.Condominium;
import pt.iscte.condo.repository.entities.Meeting;
import pt.iscte.condo.repository.entities.MeetingTopic;

import java.util.List;
import java.util.Map;

public interface PdfService {

    byte[] generateMinute(List<MeetingTopic> topics, Map<String, String> aiSummarizedTopics, Condominium condominium, Meeting meeting) throws Exception;

}
