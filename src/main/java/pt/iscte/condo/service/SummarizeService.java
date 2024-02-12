package pt.iscte.condo.service;

import pt.iscte.condo.controller.dto.request.DocumentRequest;

import java.util.Map;

public interface SummarizeService {

    Map<String, String> summarizeDocument(DocumentRequest request);

}
