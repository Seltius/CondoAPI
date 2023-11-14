package pt.iscte.condo.mapper;

import org.mapstruct.Mapper;
import pt.iscte.condo.controller.request.DocumentRequest;
import pt.iscte.condo.controller.response.DocumentResponse;
import pt.iscte.condo.domain.Document;
import pt.iscte.condo.domain.User;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DocumentMapper {

    public abstract Document documentRequestToDocument(DocumentRequest documentRequest);

    public abstract DocumentResponse documentToDocumentResponse(Document document);

    public abstract List<DocumentResponse> documentListToDocumentListResponse(List<Document> documentList);

    protected String userToUserName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    protected String bytesToString(byte[] bytes) {
        return bytes == null ? null : Base64.getEncoder().encodeToString(bytes);
    }

    protected byte[] stringToBytes(String string) {
        return string == null ? null : Base64.getDecoder().decode(string);
    }

}
