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
import pt.iscte.condo.enums.Role;
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
        User uploader = getUserByBearer(getBearer(httpRequest));
        validateAdminRole(uploader);
        validateFile(request.getFileData());
        storeDocument(request, uploader);
    }

    @Override
    public FileResponse getDocument(Integer id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        User user = getUserByBearer(getBearer(httpRequest));
        if (!user.getId().equals(document.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");

        return documentMapper.documentToDocumentResponse(document);
    }

    @Override
    public List<DocumentResponse> getDocuments() {

        User owner = getUserByBearer(getBearer(httpRequest));

        List<Document> documentList = documentRepository.findAllByUserId(owner.getId())
                .orElseThrow(() -> new RuntimeException("No documents found"));

        return documentMapper.documentListToDocumentsListResponse(documentList);
    }

    private String getBearer(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }

    }

    private void storeDocument(DocumentRequest request, User uploader) {
        User owner =  userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Document document = documentMapper.documentRequestToDocument(request);
        document.setUser(owner);
        document.setUploader(uploader);
        document.setUploadDate(LocalDateTime.now());

        documentRepository.save(document);

    }

    private void validateAdminRole(User uploader) {
        if (!uploader.getRole().equals(Role.ADMIN))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
    }

    private User getUserByBearer(String token) {
        return userRepository.findByEmail(jwtService.getUsername(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void validateFile(String base64File) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64File);
        Tika tika = new Tika();
        String fileType = tika.detect(decodedBytes);

        if (!fileType.equals("application/pdf"))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "file type not supported");

        if(decodedBytes.length > 2000000)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "file size too big");

    }


}
