package pt.iscte.condo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.controller.response.DocumentResponse;
import pt.iscte.condo.domain.Document;
import pt.iscte.condo.repository.DocumentRepository;
import pt.iscte.condo.service.DocumentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private DocumentRepository documentRepository;

    @Override
    public Document uploadDocument(DocumentRequest request) {

        //TODO
        // convert DocumentRequest to Document
        // logic to upload document and save it in the database
        // convert saved Document to DocumentResponse

        return null;
    }

    @Override
    public DocumentResponse getDocument(Integer id) {

        //TODO
        // logic to get a single document
        // convert Document to DocumentResponse

        return null;
    }

    @Override
    public List<DocumentResponse> getDocuments(Integer userId) {

        //TODO
        // logic to get multiple documents
        // convert each Document to DocumentResponse

        return null;
    }
}
