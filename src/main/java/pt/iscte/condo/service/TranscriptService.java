package pt.iscte.condo.service;

import pt.iscte.condo.repository.entities.Transcript;

import java.util.Map;

public interface TranscriptService {

    void processTranscripts();

    Map<String, String> processTranscript(Transcript transcript);

}
