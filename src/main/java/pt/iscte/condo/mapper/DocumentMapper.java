package pt.iscte.condo.mapper;

import org.mapstruct.Mapper;
import pt.iscte.condo.controller.dto.request.DocumentRequest;
import pt.iscte.condo.controller.dto.response.DocumentResponse;
import pt.iscte.condo.controller.dto.response.FileResponse;
import pt.iscte.condo.domain.Document;
import pt.iscte.condo.domain.User;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DocumentMapper {

    public abstract Document documentRequestToDocument(DocumentRequest documentRequest);

    public abstract FileResponse documentToDocumentResponse(Document document);

    public abstract List<DocumentResponse> documentListToDocumentsListResponse(List<Document> documentList);

    protected String userToUserName(User user) {
        return user.getName();
    }

    protected String bytesToString(byte[] bytes) {
        return bytes == null ? null : Base64.getEncoder().encodeToString(bytes);
    }

    protected byte[] stringToBytes(String string) {
        return string == null ? null : Base64.getDecoder().decode(string);
    }

}
