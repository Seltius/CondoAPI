package pt.iscte.condo.service;

import pt.iscte.condo.repository.entities.MeetingTopic;
import pt.iscte.condo.repository.entities.Transcript;

import java.util.List;
import java.util.Map;

public interface TranscriptService {

    Map<String, String> processTranscript(Transcript transcript, List<MeetingTopic> meetingTopics);

}
