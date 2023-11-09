package pt.iscte.condo.service;

import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.controller.response.DocumentResponse;
import pt.iscte.condo.domain.Document;

import java.util.List;

public interface DocumentService {

    Document uploadDocument(DocumentRequest request);

    DocumentResponse getDocument(Integer id);

    List<DocumentResponse> getDocuments(Integer userId);

}
