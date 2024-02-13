package pt.iscte.condo.service.mapper;

import org.mapstruct.Mapper;
import pt.iscte.condo.controller.dto.request.DocumentRequest;
import pt.iscte.condo.controller.dto.response.DocumentResponse;
import pt.iscte.condo.controller.dto.response.FileResponse;
import pt.iscte.condo.repository.entities.Document;
import pt.iscte.condo.repository.entities.User;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document documentRequestToDocument(DocumentRequest documentRequest);

    FileResponse documentToDocumentResponse(Document document);

    List<DocumentResponse> documentListToDocumentsListResponse(List<Document> documentList);

    default String userToUserName(User user) {
        return user.getName();
    }

    default String bytesToString(byte[] bytes) {
        return bytes == null ? null : Base64.getEncoder().encodeToString(bytes);
    }

    default byte[] stringToBytes(String string) {
        return string == null ? null : Base64.getDecoder().decode(string);
    }

}
