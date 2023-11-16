package pt.iscte.condo.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.controller.response.DocumentResponse;
import pt.iscte.condo.controller.response.FileResponse;
import pt.iscte.condo.domain.Document;
import pt.iscte.condo.domain.User;
import pt.iscte.condo.exceptions.BusinessException;
import pt.iscte.condo.mapper.DocumentMapper;
import pt.iscte.condo.repository.DocumentRepository;
import pt.iscte.condo.repository.UserRepository;
import pt.iscte.condo.service.DocumentService;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;
    private final HttpServletRequest httpRequest;
    private final JwtServiceImpl jwtService;

    @Override
    public void uploadDocument(DocumentRequest request) {
        String fileData = request.getFileData();

        if (!isPdfFile(fileData)) {
            throw new BusinessException("fileType not allowed (only pdf)");
        }

        User owner =  userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new BusinessException("User does not exist")); //TODO NOT WORKING (FIX ME)

        User uploader = getUserFromBearer(getBearer(httpRequest));

        Document document = documentMapper.documentRequestToDocument(request);

        document.setOwner(owner);
        document.setUploader(uploader);
        document.setUploadDate(LocalDateTime.now());

        documentRepository.save(document);

    }

    @Override
    public FileResponse getDocument(Integer id) {

        //TODO ONLY ALLOW DOWNLOAD IF IT'S FROM THE OWNER OR UPLOADER

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Document not found"));

        return documentMapper.documentToDocumentResponse(document);
    }

    @Override
    public List<DocumentResponse> getDocuments() {

        User owner = getUserFromBearer(getBearer(httpRequest));

        List<Document> documentList = documentRepository.findAllByOwnerId(owner.getId())
                .orElseThrow(() -> new RuntimeException("No documents found"));

        return documentMapper.documentListToDocumentsListResponse(documentList);
    }

    private String getBearer(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
            // Now you have the bearer token, you can use it as needed
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }

    }

    private User getUserFromBearer(String token) {
        return userRepository.findByEmail(jwtService.getUsername(token))
                .orElseThrow(() -> new BusinessException("User does not exist"));
    }

    private boolean isPdfFile(String base64File) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64File);
        Tika tika = new Tika();
        String fileType = tika.detect(decodedBytes);
        return fileType.equals("application/pdf");
    }


}
