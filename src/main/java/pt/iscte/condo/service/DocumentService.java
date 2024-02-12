package pt.iscte.condo.service;

import pt.iscte.condo.controller.dto.request.DocumentRequest;
import pt.iscte.condo.controller.dto.response.DocumentResponse;
import pt.iscte.condo.controller.dto.response.FileResponse;

import java.util.List;

public interface DocumentService {

    void uploadDocument(DocumentRequest request);

    FileResponse getDocument(Integer id);

    List<DocumentResponse> getDocuments();

}
