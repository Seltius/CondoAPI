package pt.iscte.condo.service;

import pt.iscte.condo.domain.Transcript;

import java.util.Map;

public interface TranscriptService {

    void processTranscripts();

    Map<String, String> processTranscript(Transcript transcript);

}
