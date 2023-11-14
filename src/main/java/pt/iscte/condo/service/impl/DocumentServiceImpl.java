package pt.iscte.condo.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.controller.response.DocumentResponse;
import pt.iscte.condo.domain.Document;
import pt.iscte.condo.domain.User;
import pt.iscte.condo.exceptions.BusinessException;
import pt.iscte.condo.mapper.DocumentMapper;
import pt.iscte.condo.repository.DocumentRepository;
import pt.iscte.condo.repository.UserRepository;
import pt.iscte.condo.service.DocumentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;
    private final HttpServletRequest request;
    private final JwtServiceImpl jwtService;

    @Override
    public void uploadDocument(DocumentRequest documentRequest) {

        User owner =  userRepository.findById(documentRequest.getOwnerId())
                .orElseThrow(() -> new BusinessException("User does not exist"));

        User uploader = userRepository.findByEmail(jwtService.getUsername(getBearer(request)))
                .orElseThrow(() -> new BusinessException("User does not exist"));

        Document document = documentMapper.documentRequestToDocument(documentRequest);

        document.setOwner(owner);
        document.setUploader(uploader);
        document.setUploadDate(LocalDateTime.now());

        documentRepository.save(document);

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

    private String getBearer(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
            // Now you have the bearer token, you can use it as needed
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }

    }


}
