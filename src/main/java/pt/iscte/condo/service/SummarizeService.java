package pt.iscte.condo.service;

import pt.iscte.condo.controller.dto.request.SummarizeRequest;

public interface SummarizeService {

    void summarizeDocument(SummarizeRequest request) throws Exception;

}
