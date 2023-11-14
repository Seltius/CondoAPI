package pt.iscte.condo.service;

import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.controller.response.DocumentResponse;

import java.util.List;

public interface DocumentService {

    void uploadDocument(DocumentRequest request);

    DocumentResponse getDocument(Integer id);

    List<DocumentResponse> getDocuments(Integer userId);

}
